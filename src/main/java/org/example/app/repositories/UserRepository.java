package org.example.app.repositories;

import org.example.app.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository("userRepository")
@PropertySource("classpath:database.properties")
public class UserRepository implements org.example.app.repositories.Repository<User> {

    @Value("${database.tableName}")
    private String tableName;

    @Value("${database.emailColumn}")
    private String emailColumn;

    @Value("${database.nameColumn}")
    private String nameColumn;

    @Value("${database.passwordColumn}")
    private String passwordColumn;

    @Autowired
    private JdbcOperations jdbcOperations;

    public static final String selectQuery = "SELECT * FROM \"%s\" WHERE \"%s\" = ?;";
    public static final String updateQuery = "UPDATE \"%s\" SET \"%s\" = ?, \"%s\" = ? WHERE \"%s\" = ?;";

    @Override
    public User select(Object email) {
        String query = String.format(selectQuery, tableName, emailColumn);

        Object[] objects = {email};
        int[] types = {Types.VARCHAR};

        SqlRowSet rowSet = jdbcOperations.queryForRowSet(query, objects, types);
        if (rowSet.next()) {
            User user = new User();
            user.setPassword(rowSet.getString(passwordColumn));
            user.setEmail(rowSet.getString(emailColumn));
            user.setName(rowSet.getString(nameColumn));
            return user;
        }

        return null;
    }

    @Override
    public boolean update(User user) {
        String query = String.format(updateQuery, tableName, nameColumn, passwordColumn, emailColumn);

        Object[] objects = {user.getName(), user.getPassword(), user.getEmail()};
        int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        return jdbcOperations.update(query, objects, types) > 0;
    }
}
