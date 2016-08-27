package com.hotmail.dimmaryanto.software;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hotmail.dimmaryanto.software.domain.Mahasiswa;
import com.hotmail.dimmaryanto.software.repository.MahasiswaRepository;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
public class AppTest extends TestCase {

	@Test
	public void contextLoader() {
	}

	@Autowired
	private MahasiswaRepository repo;

	@Ignore
	@Test
	public void testSimpanDataMahasiswa() {
		Mahasiswa mhs = new Mahasiswa();
		mhs.setNim("10511148");
		mhs.setNama("Dimas Maryanto");
		repo.save(mhs);
		// setelah disimpan id dari primary key
		// akan ditambah otomatis klo null berati gagal input

		assertNotNull(repo.exists(mhs.getId()));
	}

	@Ignore
	@Test
	public void testUDataMahasiswa() {
		Mahasiswa mhs = repo.findByNim("10511148");
		assertNotNull(mhs);

		mhs.setNama("Hanif");
		repo.save(mhs);
		mhs = repo.findByNim("10511148");
		assertEquals(mhs.getNama(), "Hanif");
	}

	@Test
	public void testHapusMahasiswa() {
		Mahasiswa mhs = repo.findByNim("10511148");
		repo.delete(mhs);

		assertFalse(repo.exists(mhs.getId()));
	}

}
