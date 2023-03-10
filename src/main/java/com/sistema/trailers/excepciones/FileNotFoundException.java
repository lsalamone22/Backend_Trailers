package com.sistema.trailers.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND) // no se encontrĂ³ el archivo
public class FileNotFoundException extends RuntimeException {// es una clase personalisada

	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String mensaje) {
		super(mensaje);
	}

	public FileNotFoundException(String mensaje, Throwable excepcion) {
		super(mensaje, excepcion);
	}
}
