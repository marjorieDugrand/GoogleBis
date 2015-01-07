package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.WordBean;
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
    
    private static final String WORD_DELETE = 
            "DELETE FROM WORDS "
            + "WHERE word = ?";
    
    private static final String DELETE_TABLE = 
            "DELETE FROM WORDS";
    
    public void createWord(WordBean word) {
        DAOUtilities.executeCreate(WORD_CREATE, word.getWord());
    }

    public WordBean readWordByName(String name) {
        WordBean word = null;
        ResultSet resultSet = null;
        try {
            resultSet = DAOUtilities.executeQuery(WORD_READ, name);
            if (resultSet != null && resultSet.next() ) {
                word = map(resultSet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(resultSet);
        }
        return word;
    }

    private WordBean map(ResultSet resultSet) throws SQLException {
        WordBean word = new WordBean();
        word.setId(resultSet.getInt("word_id"));
        word.setWord(resultSet.getString("word"));
        return word;
    }

    public void deleteWordByName(String name) {
        DAOUtilities.executeDelete(WORD_DELETE, name);
    }

    public void deleteTable() {
        DAOUtilities.executeDelete(WORD_READ);
    }
    
}
