import java.util.List;
import java.util.*;

public class db_connection {
     public static void main(String[] args) {
        // Create the database
        helper_functions.createDatabase();

        // Execute the schema
        helper_functions.executeSchema();
     
    }

}
