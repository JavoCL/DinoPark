package cl.duoc.dinopark.dao;

import cl.duoc.dinopark.model.EspecieDinosaurio;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieHerbivoro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecieDaoMysql implements EspecieDao {

    @Override
    public void guardarEspecies(List<EspecieDinosaurio> listaEspecies) throws Exception {
        // En bases de datos relacionales, normalmente guardamos de a uno,
        // o limpiamos y reinsertamos. Aquí haremos un ejemplo insertando registros:
        String sql = "INSERT INTO especie_dinosaurio (nombre_especie, periodo, peso, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDb.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (EspecieDinosaurio dino : listaEspecies) {
                stmt.setString(1, dino.getNombreEspecie());
                stmt.setString(2, dino.getPeriodo());
                stmt.setFloat(3, dino.getPeso());

                // Determinamos el tipo según la clase hija
                if (dino instanceof EspecieCarnivoro) {
                    stmt.setString(4, "Carnivoro");
                } else {
                    stmt.setString(4, "Herbivoro");
                }

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<EspecieDinosaurio> cargarEspecies() throws Exception {
        List<EspecieDinosaurio> lista = new ArrayList<>();
        String sql = "SELECT * FROM especie_dinosaurio";

        try (Connection conn = ConexionDb.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 🟢 1. Recuperamos el ID que genera la base de datos automáticamente (AUTO_INCREMENT)
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre_especie");
                String periodo = rs.getString("periodo");
                float peso = rs.getFloat("peso");
                String tipo = rs.getString("tipo");

                EspecieDinosaurio dino;

                // 2. Instanciamos según corresponda
                if (tipo.equals("Carnivoro") == true) {
                    dino = new EspecieCarnivoro(String.valueOf(id), nombre, periodo, peso, 5); // 5 por defecto o según tus constructor
                } else {
                    dino = new EspecieHerbivoro(String.valueOf(id), nombre, periodo, peso, "Helechos");
                }

                lista.add(dino);
            }
        }
        return lista;
    }
}