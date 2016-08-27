# Belajar Spring Data JPA

Mengakses Database Menggunakan ORM Hibernate dengan bantuan Autowiring dari Spring Framework Data JPA. Tujuannya supaya tidak perlu menulis Data Akses Object atau JDBC statement.

## Membuat project dengan Maven

```bash
mvn archetype:generate
  -DartifactId=belajar.spring-data-jpa
  -DgroupId=com.hotmail.dimmaryanto.software
  -Dversion=1.0
  -DarchetypeCatalog='internal'
  -DarcehtypeArtifactId=maven-archetype-quickstart
```

## Tambahkan dependency Spring Framework

Tambahkan dependency spring framework di file `pom.xml`

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>4.3.2.RELEASE</version>
</dependency>
<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-jpa</artifactId>
  <version>1.10.2.RELEASE</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-test</artifactId>
  <version>4.3.2.RELEASE</version>
</dependency>
```

dan tambahkan kebutuhan untuk databasenya, karena saya pake PostgreSQL maka tambahkan dependency seperti berikut:

```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-entitymanager</artifactId>
  <version>5.1.0.Final</version>
</dependency>
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>5.1.3.Final</version>
</dependency>

<!-- database driver -->
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>9.4-1206-jdbc42</version>
</dependency>
<dependency>
  <groupId>commons-dbcp</groupId>
  <artifactId>commons-dbcp</artifactId>
  <version>1.4</version>
</dependency>
```

## Setup SpringFramework.

Untuk menggunakan Spring Data JPA, kita harus setup dulu SpringFramework corenya. kita pake aja kelas yang udah dibuatkan oleh `maven-archetype-quickstart` yaitu kelas `App` sebagai default configurasi springframework dan main method seperti berikut:

```java
// package here

// import here
@Configurastion
public class App{

  public static void main(String[] args){
    new AnnotationApplicationContext(App.class);
  }
}
```

sekerang coba running menggunakan perintah maven `exec` seperti berikut:

```bash
mvn clean
    compile
    exec:java -Dexec.mainClass=com.hotmail.dimmaryanto.software.App
```

Kalo spring frameworknya udah berhasil ke load kita lanjut ke tahap berikutnya.

## Aktifkan Spring Data JPA

Untuk mengaktifkan Spring Data JPA, lumanya mudah dengan menggunakan Java Class atau annotation yaitu kita cukup tambahkan

```java
@EnableJpaRepository(
  basePackages={"folder.yang.memiliki.interface.CrudRepository"}
)
```

Setelah itu baru kita tentukan bagaiman konfigurasi database mau pake apa? kali ini saya pake PostgreSQL seperti yang telah saya sebutkan tadi dengan menggunakan Hibernate. tahap selanjutnya adalah karena kita udah mengaktifkan konfigurasi JPA maka kita harus membuat configurasi si JPA.a yaitu menggunakan `entityManagerFactory` dan juga `dataSource` atau `Connection`

## Setup `DataSource`

DataSource ini berkaitan dengan connection atau setara dengan JDBC. jadi kita buat dulu databasenya yaitu `belajar_spring_data_jpa` seperti berikut:

```sql
create user belajar_spring_data_jpa with superuser login password 'admin';

create database belajar_spring_data_jpa with owner belajar_spring_data_jpa;
```

Setelah itu kita kembali lagi ke kelas `App` untuk setting `DataSource` yaitu seperti Berikut dan tidak lupa tambahkan supaya database tersebut menjadi sebuah bean:

```java
@Bean
public DataSource dataSource() {
  BasicDataSource dbcp = new BasicDataSource();

  dbcp.setDriverClassName("org.postgresql.Driver");
  dbcp.setUrl("jdbc:postgresql://localhost:5432/belajar_spring_data_jpa");
  dbcp.setUsername("belajar_spring_data_jpa");
  dbcp.setPassword("admin");

  return dbcp;
}
```

## Setup `EntityManagerFactory` dan `TransactionManager`

Sebenarnya kita bisa meggunakan object lain seperti `LocalSessionFactoryBean` tapi kali ini saya pake yang genaral aja, jadi klo nanti suatu saat kita ganti ke `EclipseLink` tidak perlu merubah konfigurasi tapi kita cukup ganti aja dependency `hibernate-entitymanager` ke `eclipselink`. Seperti berikut konfigurasinya:

```java
@Bean
@Autowired
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
  LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
  // untuk menentukan package nama yang akan discan
  factory.setPackagesToScan("com.hotmail.dimmaryanto.software.domain");
  // setting datasource
  factory.setDataSource(ds);

  HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
  // set hibernate dialect = org.hibernate.dialect.PostgreSQL
  vendorAdapter.setDatabase(Database.POSTGRESQL);
  // set hibernate.hbm2dll.auto = true
  vendorAdapter.setGenerateDdl(true);
  // set hibernate.show-sql = true
  vendorAdapter.setShowSql(true);

  factory.setJpaVendorAdapter(vendorAdapter);
  return factory;
}

@Bean
@Autowired
public JpaTransactionManager transactionManager(EntityManagerFactory session) {
  JpaTransactionManager jpaTM = new JpaTransactionManager(session);
  return jpaTM;
}
```

## Membuat domain

```java
package com.hotmail.dimmaryanto.software.domain;

// import tidak ditampilkan

@Entity
@Table(name = "m_mahasiswa")
public class Mahasiswa(){

  @Id
  @GenericGenerator(name = "id_mahasiswa", strategy = "uuid2")
  @GeneratedValue(generator = "id_mahasiswa")
  private String id;

  private String nim;
  private String nama;

  // setter & getter here

}
```

Setelah itu anda bisa ngecek apakah tabelnya udah digenerate secara otomatis? caranya jalakan apa pake maven plugin `exec:java` seperti contoh diatas tadi.

## Membuat `repository` untuk melakukan CRUD

Untuk melakukan operasi CRUD atau singkatan dari Create Read Update Delete kita tidak perlu lagi menggunakan cara lama seperti menggunakan JDBC yaitu buka tutup koneksi, set value per kolom, membuat query sql untuk data manipulation language atau DML. sekarang kita udah disedikan kelas yang cukup ampuh yaitu `CrudRepository` atau `PagingAndSortingRepository`. jadi kelas-kelas tersebut pada dasarnya telah memiliki fitur seperti berikut:

* `findAll()` untuk menghasilkan semua data atau setara dengan `select * from nama_tabel`
* `save(Obj)` untuk melakukan operasi **simpan** dan **ubah**
* `delete(Obj)` untuk mehapus object yang ada dalam database.
* `findById(Obj)` untuk mencari berdasarkan id atau **primary key**
* `exists(Obj)` untuk menghasilkan data yang kita cari berdasarkan id apakah ada atau tidak di dalam tabel, fungsi tersebut menghasilkan nilai boolean.

Sekarang kita buat seperti berikut:

```java
package com.hotmail.dimmaryanto.software.repository;

// import tidak ditampilkan

// CrudRepository<1, 2>
// no 1. adalah model
// no 2. adalah primary key
public interface MahasiswaRepository extends CrudRepository<Mahasiswa, String> {

}
```

Untuk lebih lengkapnya silahkan baca dokumentasi spring data jpa [berikut](http://projects.spring.io/spring-data-jpa/)
