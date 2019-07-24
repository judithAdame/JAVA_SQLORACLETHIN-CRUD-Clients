package bdventes2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDVentes2 {
        final static String USER = "vente";
        final static String PASSWORD = "anypw";
        final static String NAME = "oracle.jdbc.OracleDriver";
        final static String URL = "jdbc:oracle:thin:@144.217.163.57:1521:XE"; 
        
    public static void main(String[] args) {
        String requeteSelectSimple = "select * from client";
        String requeteSelect = "select * from client where noclient = ?"; 
        String requeteDelete = "delete client where noClient = ?"; 
        String requeteUpdate = "update client set nomclient = ? where noclient = ?"; 
        String requeteInsert = "insert into client values (?, ?, ? )";
        int numClient = 886;     
        
        try {
            int num;

            num = InsertClient(requeteInsert, numClient, "Judith Adame Aragon", "51465478945");
            System.out.println((num>0)?"nb lignes inserées: "+num: "Aucun resultat");
            
            num = SelectClient(requeteSelect, numClient);
            System.out.println((num>0)?"nb lignes lues: "+num: "Aucun resultat");

            num = UpdateClient(requeteUpdate, "Julianna Pacheco", numClient);
            System.out.println((num>0)?"nb lignes updatées : "+num: "Aucun resultat");

            num = SelectClient(requeteSelect, numClient);
            System.out.println((num>0)?"nb lignes lues: "+num: "Aucun resultat");
            
            num = DeleteClient(requeteDelete, numClient);
            System.out.println((num>0)?"nb lignes deletées: "+num: "Aucun resultat");
            
            num = SelectClient(requeteSelect, numClient);
            System.out.println((num>0)?"nb lignes lues: "+num: "Aucun resultat");  
            
            num = SelectSimpleClient(requeteSelectSimple);
            System.out.println((num>0)?"nb lignes lues: "+num: "Aucun resultat");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BDVentes2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int SelectSimpleClient (String requete) 
        throws SQLException {
        Connection connection = connectionManager(); 
        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(requete);
        String  nomClient;
        String  noTelephone;
        int noClient;    
        
        System.out.println("noClient\t"+"noTelephone\t"+"nomClient");         
        int numSelect = 0;
        
        while (resultSet.next())
        {
            noClient = resultSet.getInt(1);
            nomClient = resultSet.getString("nomClient");
            noTelephone = resultSet.getString("noTelephone");
            System.out.println(noClient+"\t\t"+noTelephone+"\t\t"+nomClient);
            numSelect++;
        }
        
        resultSet.close();  
        stm.close(); 
        connection.close();
        return numSelect;
    }
    
    public static int SelectClient (String requete, 
                                        int noClient) 
        throws SQLException {
        Connection connection = connectionManager(); 
        PreparedStatement stm = connection.prepareStatement(requete);
        stm.setInt(1, noClient);
        ResultSet resultSet = stm.executeQuery();
        String  nomClient;
        String  noTelephone;
        
        System.out.println("noClient\t"+"noTelephone\t"+"nomClient");         
        int numSelect = 0;
        
        while (resultSet.next())
        {
            noClient = resultSet.getInt(1);
            nomClient = resultSet.getString("nomClient");
            noTelephone = resultSet.getString("noTelephone");
            System.out.println(noClient+"\t\t"+noTelephone+"\t\t"+nomClient);
            numSelect++;
        }
        
        resultSet.close();  
        stm.close(); 
        connection.close();
        return numSelect;
    }
        
    public static int UpdateClient (String requete, 
                                    String nomClient, 
                                    int noClient) 
            throws SQLException {        
            Connection connection = connectionManager();            
            PreparedStatement stm = connection.prepareStatement(requete);
            stm.setString(1, nomClient);
            stm.setInt(2, noClient);
            int numero = stm.executeUpdate();
            closeManager(stm, connection);
            return numero;
    }
        
    public static int InsertClient (String requete, 
                                    int noClient, 
                                    String nomClient, 
                                    String noTelephone) 
            throws SQLException {        
            Connection connection = connectionManager();            
            PreparedStatement stm = connection.prepareStatement(requete);
            stm.setInt(1, noClient);
            stm.setString(2, nomClient);
            stm.setString(3,noTelephone);
            int numero = stm.executeUpdate();
            closeManager(stm, connection);
            return numero;
    }
    
    public static int DeleteClient (String requete, 
                                    int noClient) 
        throws SQLException, ClassNotFoundException {
        Connection connection = connectionManager();            
        PreparedStatement stm = connection.prepareStatement(requete);
        stm.setInt(1, noClient);
        int numero = stm.executeUpdate();
        closeManager(stm, connection);
        return numero;
    }
        
    private static Connection connectionManager() {
            try {
                Class.forName(NAME);
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                return connection;
            } catch(ClassNotFoundException cnfe){
                System.out.println("Driver introuvable : ");
                Logger.getLogger(BDVentes2.class.getName()).log(Level.SEVERE, null, cnfe);
                cnfe.printStackTrace();
                return null;
            }
            catch(SQLException sqle){
               System.out.println("Erreur SQL : ");
               //Cf. Comment gérer les erreurs 
               return null;
            }
            catch(Exception e){
               System.out.println("Autre erreur : ");
               e.printStackTrace();
               return null;
            }
    }

    private static void closeManager(PreparedStatement stm, 
                                     Connection acces) {
            try {
                stm.close();
                acces.close();
            } catch (SQLException ex) {
                Logger.getLogger(BDVentes2.class.getName()).log(Level.SEVERE, null, ex);
            }
    }    
}