package com.ygrs.eqcontrol.Objects;

/**
 * Created by Gabriela on 20/03/2017.
 */

public class User {
    String Nombre;
    String Apellido_Paterno;
    String Apellido_Materno;
    String Carrera;
    String Matricula;
    String Contrasena;
    String Foto;
    String Tipo_de_usuario;
    String Vigencia_de_registro;

    public User() {
    }

    public User(String nombre, String apellido_Paterno, String apellido_Materno, String carrera, String matricula, String contrasena, String foto, String tipo_de_usuario, String vigencia_de_registro) {
        Nombre = nombre;
        Apellido_Paterno = apellido_Paterno;
        Apellido_Materno = apellido_Materno;
        Carrera = carrera;
        Matricula = matricula;
        Contrasena = contrasena;
        Foto = foto;
        Tipo_de_usuario = tipo_de_usuario;
        Vigencia_de_registro = vigencia_de_registro;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido_Paterno() {
        return Apellido_Paterno;
    }

    public void setApellido_Paterno(String apellido_Paterno) {
        Apellido_Paterno = apellido_Paterno;
    }

    public String getApellido_Materno() {
        return Apellido_Materno;
    }

    public void setApellido_Materno(String apellido_Materno) {
        Apellido_Materno = apellido_Materno;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getTipo_de_usuario() {
        return Tipo_de_usuario;
    }

    public void setTipo_de_usuario(String tipo_de_usuario) {
        Tipo_de_usuario = tipo_de_usuario;
    }

    public String getVigencia_de_registro() {
        return Vigencia_de_registro;
    }

    public void setVigencia_de_registro(String vigencia_de_registro) {
        Vigencia_de_registro = vigencia_de_registro;
    }
}