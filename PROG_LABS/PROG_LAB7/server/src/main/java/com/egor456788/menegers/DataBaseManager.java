package com.egor456788.menegers;

import com.egor456788.Applicaton;
import com.egor456788.common.Conditions;
import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBaseManager {
    private static Connection connection;
    private static Session session;

    static {
        JSch jsch = new JSch();

        String host = "se.ifmo.ru";
        String user = "s412905";
        String privateKey = "~/.ssh/id_rsa";
        int port = 2222;

        String jdbcURL = "jdbc:postgresql://localhost:5432/studs";
        String databaseHost = "pg";
        String databaseUser = "s412905";
        String databasePassword = "PzifW6F1NegAkSI7";

        int localPort = 5432;

        try {
            session = jsch.getSession(user, host, port);
            session.setConfig("PreferredAuthentications", "publickey");
            jsch.setKnownHosts("~/.ssh/known_hosts");
            jsch.addIdentity(privateKey);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            session.setPortForwardingL(localPort, databaseHost, 5432);

            System.setProperty("jdbc.url", jdbcURL);
            System.setProperty("jdbc.user", databaseUser);

            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(jdbcURL, databaseUser, databasePassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(String userName, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts (username, password) VALUES (?, ?)");

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Applicaton.logger.info("Пользователь " + userName + " успешно добавлен");
            } else {
                Applicaton.logger.info("Произошла ошибка. Пользователь " + userName + " не добавлен");
            }

            preparedStatement.close();


        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public static int addEntity(Entity entity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO entities (name, condition, devotion, race, score, age, height, weight, gender, creator_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getName() );
            preparedStatement.setString(2, entity.getCondition().name());
            preparedStatement.setString(3, entity.getDevotion().name());
            preparedStatement.setString(4, entity.getRace().name());
            preparedStatement.setInt(5, entity.getScore());
            preparedStatement.setInt(6, entity.getAge());
            preparedStatement.setInt(7, entity.getHeight());
            preparedStatement.setInt(8, entity.getWeight());
            preparedStatement.setString(9, entity.getGender().name());
            preparedStatement.setString(10, entity.getCreatorName());


            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                long id = -1;
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                    Applicaton.logger.info("Существо " + entity + " успешно добавлено с ID: " + id);
                } else {
                    Applicaton.logger.info("Не удалось получить ID для существа: " + entity);
                }
                generatedKeys.close(); // Close the ResultSet after you're done with it
                preparedStatement.close();
                return (int) id;
            } else {
                Applicaton.logger.info("Не удалось добавить существо: " + entity);
                preparedStatement.close();
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;

        }
    }
    public static void updateEntity(Entity entity)  {


        try {

            String query =
                    "UPDATE entities SET name = ?, condition = ?, devotion = ?, race = ?, score = ?, age = ?, height = ?,weight = ?,gender = ?,creator_name = ? WHERE ent_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCondition().name());
            preparedStatement.setString(3, entity.getDevotion().name());
            preparedStatement.setString(4, entity.getRace().name());
            preparedStatement.setInt(5, entity.getScore());
            preparedStatement.setInt(6, entity.getAge());
            preparedStatement.setInt(7, entity.getHeight());
            preparedStatement.setInt(8, entity.getWeight());
            preparedStatement.setString(9, entity.getGender().name());
            preparedStatement.setString(10, entity.getCreatorName());
            preparedStatement.setLong(11, entity.getId());


            preparedStatement.executeUpdate();

            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fill(CollectionMeneger collectionMeneger,ReentrantReadWriteLock lock)  {
        lock.readLock().lock();
        try {
            collectionMeneger.setCreationDate(LocalDateTime.now());
            String query =
                    "SELECT entities.ent_id as id, entities.name as name, entities.condition as condition,entities.devotion as devotion, entities.race as race, score,age, height,weight, entities.gender as gender, entities.creator_name as creator_name  FROM entities";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            collectionMeneger.clear();

            while (resultSet.next()) {
                Entity entity = null;
                if(resultSet.getString("race").equals(Races.Hattifattner.name())) {
                     entity = new Hattifattener(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            Devotions.valueOf(resultSet.getString("devotion")),
                            resultSet.getInt("age"),
                            resultSet.getInt("height"),
                            resultSet.getInt("weight"),
                            Genders.valueOf(resultSet.getString("gender")),
                            Races.valueOf(resultSet.getString("race")),
                            resultSet.getString("creator_name")

                            );
                }
                else {
                     entity = new Hemulen(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("age"),
                            resultSet.getInt("height"),
                            resultSet.getInt("weight"),
                            Genders.valueOf(resultSet.getString("gender")),
                            Races.valueOf(resultSet.getString("race")),
                            resultSet.getString("creator_name")

                            );

                }
                entity.setCondition(Conditions.valueOf(resultSet.getString("condition")));
                collectionMeneger.add(entity);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void fillAcc(ReentrantReadWriteLock lock)  {
        lock.readLock().lock();
        try {

            String query =
                    "SELECT accounts.user_id as id, accounts.username as name, accounts.password as password FROM accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            AccountsMeneger.clear();

            while (resultSet.next()) {
                    AccountsMeneger.register(resultSet.getString("name"),resultSet.getString("password"));

            }

            resultSet.close();
            preparedStatement.close();
            Applicaton.logger.info("Accounts: " +AccountsMeneger.getAcc());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

}
