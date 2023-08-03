package com.example.soap;

public class Computadora {
    private String com_serie;
    private String com_marca;
    private String com_so;

    public Computadora(String com_serie, String com_marca, String com_so) {
        this.com_serie = com_serie;
        this.com_marca = com_marca;
        this.com_so = com_so;
    }

    public String getCom_serie() {
        return com_serie;
    }

    public void setCom_serie(String com_serie) {
        this.com_serie = com_serie;
    }

    public String getCom_marca() {
        return com_marca;
    }

    public void setCom_marca(String com_marca) {
        this.com_marca = com_marca;
    }

    public String getCom_so() {
        return com_so;
    }

    public void setCom_so(String com_so) {
        this.com_so = com_so;
    }
}
