package com.sistema.trailers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.trailers.servicio.AlmacenServicioImpl;


@RestController
@RequestMapping("/assets")//este controlor va a estar mapeado a la ruta de assets

public class AssetsControlador {
		
	@Autowired
	private AlmacenServicioImpl servicio;

	
	@GetMapping("/{filename:.+}")//esto es para indicarle la extension jpg pnj
	public Resource obtenerRrecurso(@PathVariable("filename") String filename) {//le añadimos un filename al pathvariable
		return servicio.cargarComoRecurso(filename);							//para que le pases al index ya que ahí estan el filename
		
	}
		



}


