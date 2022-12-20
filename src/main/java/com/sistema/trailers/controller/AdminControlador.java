package com.sistema.trailers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.modelo.Genero;
import com.sistema.trailers.modelo.Pelicula;
import com.sistema.trailers.repositorios.GeneroRepositorio;
import com.sistema.trailers.repositorios.PeliculaRepositorio;
import com.sistema.trailers.servicio.AlmacenServicioImpl;

@Controller
@RequestMapping("/admin")//este controlor va a estar mapeado a la ruta de admin
public class AdminControlador {//este admin controller va a estar mapeado con el Administración en el nav bar
								// para que, cuando entremos, nos muestre todo

	
	@Autowired
	private PeliculaRepositorio peliculaRepositorio;
	
	@Autowired
	private GeneroRepositorio generoRepositorio;
	
	@Autowired
	private AlmacenServicioImpl servicio;
	
	
	
	
	@GetMapping("")
	public ModelAndView verPaginaDeInicio(@PageableDefault(sort ="titulo",size=5)Pageable pageable) {//@PageableDefault se va a paginar deacuerdo de la a -z
		
		Page<Pelicula> peliculas = peliculaRepositorio.findAll(pageable);// lo va a buscar todo y le paginas la paginación como se va a estar paginando o como se va a ordenar por el titulo
		return new ModelAndView("admin/index").addObject("pelicula",peliculas);
		//arriba te va a  reotrnar a un archivo html index y le va a añador un modelo pelicula
	}
	
	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormularioDeNuevaPelicula() {//aqui vamos a mostrar el form para registrar una nueva pelicula
		List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
		return new ModelAndView("admin/nueva-pelicula")//vamos a retornar al carpeta admin y archivo nueva-pelicula
						.addObject("pelicula", new Pelicula())
						//el primero obj que vamos a crear es una nueva instancia/objeto de pelicula
						//ya que a esta nueva instancia u objecto le vamos a asiganar los atributos
						.addObject("generos", generos);
						//aqui añadimos otro objecto que será la lista de generos para indicarle un combo box que me seleccione los generos
						//ahora en templates vamos a crear una carpeta llamada admin y crearemos el archivo html nueva-pelicula
		
	}
	
	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Pelicula pelicula,BindingResult bindingResult) {
		if(bindingResult.hasErrors() || pelicula.getPortada().isEmpty()) {//si es que yo tengo algun error
			if(pelicula.getPortada().isEmpty()) {
				bindingResult.rejectValue("portada", "MultipartNotEmpty");//ese MultipartNotEmpty para despues añadirle un msj de validación
			}
			
			List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
			return new ModelAndView("admin/nueva-pelicula")
					.addObject("pelicula",pelicula)//aqui ya no será una nueva instancia, sino una peliocula ya existente
					.addObject("generos", generos);
			
		}
		
		String rutaPortada =servicio.almacenarArchivo(pelicula.getPortada());//alamacenamos el archivo 
		pelicula.setRutaPortada(rutaPortada);
		
		peliculaRepositorio.save(pelicula);
		return new ModelAndView("redirect:/admin");
		
		
	}
	
	
	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView mostrarFormularioDeEditarPelicual(@PathVariable Integer id) {
		Pelicula pelicula = peliculaRepositorio.getOne(id);
		List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
		
		return new ModelAndView("admin/editar-pelicula")
				.addObject("pelicula",pelicula)
				.addObject("generos", generos);
	}
	
	
	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView actualizarPelicula(@PathVariable Integer id,@Validated Pelicula pelicula,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {//esto es lo que voy ahcer si mi form tiene errores
			List<Genero> generos = generoRepositorio.findAll(Sort.by("titulo"));
			
			return new ModelAndView("admin/editar-pelicula")
					.addObject("pelicula",pelicula)
					.addObject("generos", generos);
		}
		
		
		Pelicula peliculaDB = peliculaRepositorio.getOne(id);
		peliculaDB.setTitulo(pelicula.getTitulo());
		peliculaDB.setSinopsis(pelicula.getSinopsis());
		peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
		peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
		peliculaDB.setGeneros(pelicula.getGeneros());
		
		if(!pelicula.getPortada().isEmpty()) {//el ! significa que tiene algo, simpremte vas a actualizar
			servicio.eliminarArchivo(peliculaDB.getRutaPortada());
			String rutaPortada = servicio.almacenarArchivo(pelicula.getPortada());//alamacenamos el archivo 
			peliculaDB.setRutaPortada(rutaPortada);
		}
		
		peliculaRepositorio.save(peliculaDB);
		return new ModelAndView("redirect:/admin");
		
	}
	
	
	@PostMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		Pelicula pelicula = peliculaRepositorio.getOne(id);//obtenemos una pelicula
		peliculaRepositorio.delete(pelicula);
		servicio.eliminarArchivo(pelicula.getRutaPortada());
		
		return "redirect:/admin";
	}
	
}
