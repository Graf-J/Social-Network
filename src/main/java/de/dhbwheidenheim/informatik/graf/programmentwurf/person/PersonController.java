package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {
	@GetMapping("/demo")
	public String personView() {
		return "index";
	}
}
