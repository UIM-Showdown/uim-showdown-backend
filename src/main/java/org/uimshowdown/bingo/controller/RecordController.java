package org.uimshowdown.bingo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.models.Record;

@RestController
public class RecordController {
	
	@Autowired
	private RecordRepository recordRepository;
	
	@GetMapping("/records")
	public Set<Record> getRecords() {
		Set<Record> records = new HashSet<Record>();
		for(Record record : recordRepository.findAll()) {
			records.add(record);
		}
		return records;
	}

}
