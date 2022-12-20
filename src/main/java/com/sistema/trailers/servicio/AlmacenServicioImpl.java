package com.sistema.trailers.servicio;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.sistema.trailers.excepciones.AlmacenExcepcion;
import com.sistema.trailers.excepciones.FileNotFoundException;

@Service
public class AlmacenServicioImpl implements AlmacenServicio{

		
	@Value("${storage.location}")
	private String storageLocation;
	
	//esta notacion sirve para indicar que este metodo se va a ejecutar cada vez
	//que haya una nueva instancia de esta clase
	//el metodo de inciarALmaceArchivos se va a inicar cada vez que nosotros hagamos una nueva instancia
	//a AlmacenServicioImpl y por eso le ponemos @PostConstruct
	@PostConstruct
	@Override
	public void inciarAlmacenDeArchivos() {
		try {
			Files.createDirectories(Paths.get(storageLocation));//vamos a pasarle la ruta con path
			//lo que estamos haciendo es indicar donde vamos a almacenar nuestras imagenens del cine
		}catch (IOException excepcion) {
			throw new AlmacenExcepcion("Error al inicializar la ubicacion en el almacen de archivos");
		}
			
		
		
	}

	@Override
	public String almacenarArchivo(MultipartFile archivo) {
		String nombreArchivo = archivo.getOriginalFilename();//obtenemos el archivo su nombre original
		
		if(archivo.isEmpty()) {
			throw new AlmacenExcepcion("No se puede almacenar un archivo vacio");
		}
		try {
			InputStream inputStream = archivo.getInputStream();//1. optienes los inputStream (flujo de entrada) del archivo getOriginalFilename
			Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo),StandardCopyOption.REPLACE_EXISTING);
			//StandardCopyOption REPLACE_EXISTING sirve para declarar que si hay un archivo con el mismo nombre lo va a reemplazar
			//en File pones input y ahora le vas a pasar  Paths.get(storageLocation).resolve(nombreArchivo) el nombre archivo de la ruta de un storage location
			// y si uno ya existe hara el StandardCopyOption
		}catch (IOException excepcion) {
			throw new AlmacenExcepcion("Error al almacenar el archivo " + nombreArchivo,excepcion);
		}
		
		return nombreArchivo;
	}

	@Override
	public Path cargarArchivo(String nombreArchivo) {
		return Paths.get(storageLocation).resolve(nombreArchivo);//estamos cargando el archivo
	}

	@Override
	public Resource cargarComoRecurso(String nombreArchivo) {
		try {
			//añadimos una ruta
			Path archivo = cargarArchivo(nombreArchivo);
			//creamos una variable
			Resource recurso = new UrlResource(archivo.toUri());//para generarlo
			
			if(recurso.exists() || recurso.isReadable()) {//redeable=legible
				return recurso;
			}else {
				throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreArchivo);
			}
			
		}catch (MalformedURLException excepcion) {
			throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreArchivo,excepcion);
		}
	}

	@Override
	public void eliminarArchivo(String nombreArchivo) {
		Path archivo = cargarArchivo(nombreArchivo);//cargamos el nombre del archivo para buscarlo
		try {
			FileSystemUtils.deleteRecursively(archivo);
		}catch (Exception excepcion) {
			System.out.println(excepcion);
		}
		
	}

}
