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

}
