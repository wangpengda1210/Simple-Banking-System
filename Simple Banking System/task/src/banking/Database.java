package banking;

import java.sql.*;

public class Database {

    private final Connection conn;

    public Database(String path) {
        conn = connectToDatabase(path);
        try {
            assert conn != null;
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createNewTable();
    }

    private Connection connectToDatabase(String path) {
        String url = "jdbc:sqlite:" + path;

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createNewTable() {
        if (conn != null) {
            try (Statement statement = conn.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "number TEXT,\n" +
                        "pin TEXT,\n" +
                        "balance INTEGER DEFAULT 0\n);");
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void insert(String cardNumber, String pin, int balance) {
        if (conn != null) {
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate("INSERT INTO card (number, pin, balance)\n" +
                        "VALUES ('" + cardNumber + "',\n'" +
                        pin + "',\n" +
                        balance + ");");
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public String queryPin(String cardNumber) {
        if (conn != null) {
            try (Statement statement = conn.createStatement()) {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT pin\n" +
                                "FROM card\n" +
                                "WHERE number = '" + cardNumber + "';"
                );
                if (resultSet.next()) {
                    return resultSet.getString("pin");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public int queryBalance(String cardNumber) {
        if (conn != null) {
            try (Statement statement = conn.createStatement()) {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT balance\n" +
                                "FROM card\n" +
                                "WHERE number = '" + cardNumber + "';"
                );
                if (resultSet.next()) {
                    return resultSet.getInt("balance");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return -1;
    }

    public void addIncome(String cardNumber, int income) {
        addIncome(cardNumber, income, false);
    }

    private void addIncome(String cardNumber, int income, boolean isTransfer) {
        if (conn != null) {
            String sql = "UPDATE card SET balance = balance + ? WHERE number = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, income);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
                if (!isTransfer) {
                    conn.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public boolean transfer(String from, String to, int amount) {
        if (conn != null) {
            if (queryBalance(from) < amount) {
                return false;
            }
            if (!isCardNumberExists(to)) {
                return false;
            }
            addIncome(from, -amount, true);
            addIncome(to, amount, true);
            try {
                conn.commit();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean isCardNumberExists(String cardNumber) {
        if (conn != null) {
            String sql = "SELECT * FROM card WHERE number = ?;";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, cardNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void closeAccount(String cardNumber) {
        if (conn != null) {
            String sql = "DELETE FROM card WHERE number = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, cardNumber);
                preparedStatement.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
