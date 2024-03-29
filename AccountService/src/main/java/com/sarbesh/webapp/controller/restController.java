package com.sarbesh.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sarbesh.webapp.model.Profile;
import com.sarbesh.webapp.model.User;
import com.sarbesh.webapp.repository.AccountRepositry;


@RestController
@RequestMapping("/account")
@CrossOrigin
public class restController {
	
	@Autowired
	private AccountRepositry accRepo;
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@GetMapping("/profiles")
	public List<Profile> search(){
		return accRepo.findAll();
	}
	
	@GetMapping("/profile/{id}")
	public Profile profile(@PathVariable("id") long id) {
		return accRepo.findById(id).orElse(null);
	}
	
	@GetMapping("/delete/{id}")
	public String delProfile(@PathVariable("id") long id) {
		accRepo.deleteById(id);
		return "Success";
	}
	
	@GetMapping("/delete")
	public String delProfiles() {
		accRepo.deleteAll();
		return "Success";
	}
	
	@PostMapping("/register")
	public User profile(@RequestBody Profile newProfile) {
		Profile response;
		if(accRepo.findByEmail(newProfile.getEmail())==null) {
			response = accRepo.save(newProfile);	
		}
		else {
			accRepo.delete(newProfile);
//			accRepo.deleteById(accRepo.findByEmail(newProfile.getEmail()).getId());;
			response = accRepo.save(newProfile);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", response.getId().toString());
		map.put("email", newProfile.getEmail());
		map.put("password", newProfile.getPassword());
		return restTemplate.postForObject("http://Auth-Service/auth/register", map, User.class);
	}
	
}
