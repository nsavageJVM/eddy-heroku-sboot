package com.eduonix;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.StringJoiner;

/**
 * Created by ubu on 08.11.16.
 * // java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 artifacts/micro_s.jar
 */
@RestController
public class TicketController {

    private static final String WELCOME_TEMPLATE= "<h1> Welcome Eddys Ticketing App</h1>";
    private static final String CREATE_TICKET_TEMPLATE= "<form method='POST' action='/createnew'>"
            + "First name:<br> <input type= 'text' name='title'> <br> content:<br><input type= 'text' name='content'> "
            + "<br> <input type='submit' name='Submit'/> </form>";

    private static final String CREATE_TICKET_RESULT_TEMPLATE= "<h1>Result</h1><br>title: %s <br>content: %s <br> " +
            "<a href='/createticket'>Create Another TICKET</a> ";

    private static final String  TICKETS_TEMPLATE= "<h1>TICKETS</h1><br>%s";


    private static  int ID = 0;

    @RequestMapping("/")
    public String home() {
        return WELCOME_TEMPLATE;
    }


    @RequestMapping(value="/createticket", method= RequestMethod.GET)
    public String createTicketForm( ) {
        return CREATE_TICKET_TEMPLATE;
    }

    @RequestMapping(value="/createnew", method= RequestMethod.POST)
    public String createUser(HttpServletRequest request) {

        String content = request.getParameter("content");

        String title = request.getParameter("title");

        int id = ID++;

        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String sql;
            sql = "insert into ticket(title, content ) values " +
                    "('" + title  + "', '" + content + "');";
            System.out.println(sql);
            stmt.execute(sql);


        }catch(Exception e){
            e.printStackTrace();
        }

      return String.format(CREATE_TICKET_RESULT_TEMPLATE, title, content );
    }


    @RequestMapping("/tickets")
    public String findAll( ) {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT id, title, content FROM ticket";
            ResultSet rs = stmt.executeQuery(sql);
            StringJoiner sb = new StringJoiner("<br>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");

                sb.add(""+id).add(title). add(content);

            }

            return String.format(TICKETS_TEMPLATE, sb.toString() );
        } catch (Exception e) {
            return e.toString();
        }
    }




    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = null;
        if(System.getenv("DATABASE_URL") != null) {
            dbUri = new URI(System.getenv("DATABASE_URL"));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                    + dbUri.getPort() + dbUri.getPath()
                    + "?sslmode=require";
            return DriverManager.getConnection(dbUrl, username, password);

        }

        else {
            Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/ticketdb",  "<your psg uname>", "<your pwwd>");
            return connection;
        }




    }







}


