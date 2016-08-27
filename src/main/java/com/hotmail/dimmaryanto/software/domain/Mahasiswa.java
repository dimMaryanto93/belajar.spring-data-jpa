package com.hotmail.dimmaryanto.software.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "m_mahasiswa")
public class Mahasiswa {

	@Id
	@GenericGenerator(name = "id_mahasiswa", strategy = "uuid2")
	@GeneratedValue(generator = "id_mahasiswa")
	private String id;

	private String nim;
	private String nama;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNim() {
		return nim;
	}

	public void setNim(String nim) {
		this.nim = nim;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

}
