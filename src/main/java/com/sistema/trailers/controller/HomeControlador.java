package com.sistema.trailers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.modelo.Pelicula;
import com.sistema.trailers.repositorios.PeliculaRepositorio;


@Controller
@RequestMapping("")
public class HomeControlador {
	
	@Autowired
	private PeliculaRepositorio peliculaRepositorio;
	
	@GetMapping("")
	public ModelAndView verPaginaDeInicio() {
		List<Pelicula> ultimasPeliculas = peliculaRepositorio.findAll(PageRequest.of(0, 4, Sort.by("fechaEstreno").ascending())).toList();//el page request.off es para pasarle una pagina del 0-4
		//to lIst para que devuelva en lista y ascending par que se vea en forma descendente
		
		return new ModelAndView("index")
				.addObject("ultimasPeliculas", ultimasPeliculas);
				//el obj que le vamos a añadir son las ultimas peliculas(los vamos a listar desde un html)
	}
	
	
	@GetMapping("/peliculas")
	public ModelAndView listarPeliculas(@PageableDefault(sort = "fechaEstreno", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Pelicula> peliculas = peliculaRepositorio.findAll(pageable);
		return new ModelAndView("peliculas")
				.addObject("peliculas",peliculas);
	}
	//aqui le estamos indicando que lo vamos a ordenar por la fecha de estreno de manera descendete
	//y le pasamos las peliculas para mostrarlas en el List

	
	@GetMapping("/peliculas/{id}")
	public ModelAndView mostrarDetallesDePelicula(@PathVariable Integer id) {//cuando yo quiera ver los detalles de una peli , como su trailer datos, sinopsis
		Pelicula pelicula = peliculaRepositorio.getOne(id);
		return new ModelAndView("pelicula").addObject("pelicula",pelicula);
		//model and viw le pasamos un apeli y le pasamos un addObject y le pasamos pelicula y el obj pelicula
		
	}
	
	
}
