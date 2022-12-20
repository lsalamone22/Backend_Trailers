package com.sistema.trailers.servicio;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AlmacenServicio {

	public void inciarAlmacenDeArchivos();

	public String almacenarArchivo(MultipartFile archivo);

	public Path cargarArchivo(String nombreArchivo);

	public Resource cargarComoRecurso(String nombreArchivo);// estos son los metodos de servicios que vamos a
															// implementar

	public void eliminarArchivo(String nombreArchivo);
}
