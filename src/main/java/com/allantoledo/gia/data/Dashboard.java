package com.allantoledo.gia.data;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

@Repository
public class Dashboard {


    Connection connection;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/gia";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "123456");
        return DriverManager.getConnection(url, props);
    }

    public int countItensApreendidosNoAno(String ano) {
        try {
            connection = getConnection();
            String sql = "SELECT count(*) FROM item_apreendido WHERE EXTRACT('Year' FROM data_apreensao) = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(ano));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt("count");
            connection.close();
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    public BigDecimal sumValorDosItensApreendidosNoAno(String ano) {
        try {
            connection = getConnection();
            String sql = "SELECT coalesce(sum(valor_avaliado), 0) as sum FROM item_apreendido WHERE EXTRACT('Year' FROM data_apreensao) = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(ano));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getBigDecimal("sum");
            connection.close();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    public String categoriaComMaisApreensoes(String ano) {
        try {
            connection = getConnection();
            String sql = "SELECT count(ia.id) as cnt, ci.nome_categoria, ci.id FROM item_apreendido ia " +
                    "INNER JOIN item_apreendido_categorias iac on ia.id = iac.item_apreendido_id " +
                    "INNER JOIN categoria_item ci on ci.id = iac.categorias_id " +
                    "WHERE EXTRACT('Year' FROM data_apreensao) = ? " +
                    "GROUP BY ci.id ORDER BY cnt DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(ano));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getString("nome_categoria") + " com " + rs.getInt("cnt") + " apreensões";
            connection.close();
        } catch (SQLException e) {
            return "Nenhuma";
        }
        return "Nenhuma";
    }

    public int quantidadeDeItemsDoados(String ano) {
        try {
            connection = getConnection();
            String sql = "SELECT count(ia.id) as cnt FROM item_apreendido ia " +
                    "WHERE EXTRACT('Year' FROM data_apreensao) = ? AND " +
                    "estado_do_objeto = 'ENVIADO_PARA_DOACAO'  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(ano));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt("cnt");
            connection.close();
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

}
