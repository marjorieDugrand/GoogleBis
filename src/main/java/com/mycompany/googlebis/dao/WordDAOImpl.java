package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.beans.WordBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO object used to manipulate words in the database
 * @author Marjorie
 */
public class WordDAOImpl implements WordDAO {
    
    private static final String WORD_CREATE = 
            "INSERT INTO Words (word) "
          + "VALUES (?)";
   
    private static final String WORD_READ =
            "SELECT word_id, word "
          + "FROM words "
          + "WHERE word = ?";
    
    public void createWord(WordBean word) {
        DAOUtilities.executeUpdate(WORD_CREATE, word.getWord());
    }

    public WordBean readWordByName(String name) {
        ResultSet resultSet = DAOUtilities.executeQuery(WORD_READ, name);
        WordBean word = null;
        try {
            if (resultSet != null && resultSet.next() ) {
                word = map(resultSet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return word;
    }

    private WordBean map(ResultSet resultSet) throws SQLException {
        WordBean word = new WordBean();
        word.setId(resultSet.getInt("word_id"));
        word.setWord(resultSet.getString("word"));
        return word;
    }
    
}
