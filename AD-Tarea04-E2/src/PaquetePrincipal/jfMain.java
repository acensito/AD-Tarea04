package PaquetePrincipal;

//librerías JDBC
import java.sql.*;

//librerias necesarias para el entorno gráfico
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;

/**
 *
 * @author IMCG
 */
public class jfMain extends javax.swing.JFrame {
  
  //URL de la base de datos anaconda
  String url = "jdbc:postgresql://localhost/anaconda";
  
  //contraseña del usuario postgres para acceder a la base de datos anaconda
  String passwd = "123456";

  /****************************************************************************
   * constructor del formulario con excepcion verificada de tipo SQLException
   * 
   * @throws SQLException 
   */
  public jfMain() throws SQLException {
    
    //componentes especificados en la ficha 'Diseño'
    initComponents();

    //conexión
    Connection conn = null;

    try {
      //abre la conexión con la base de datos a la que apunta el url
      //mediante la contraseña del usuario postgres
      conn = DriverManager.getConnection(url, "postgres", passwd);

      //elimina (si existen) todos los objetos que van a crearse
      drop_Ejemplo(conn);

      //crea los tipos compuestos y la función del gestor
      crear_Tipos(conn);

      //crea la tabla
      crear_Tabla(conn);

      //introduce algunos registros de ejemplo
      insertar_Registros(conn);

      //rellena el cuadro combinado
      rellenar_JComboBox(conn);

    } catch (SQLException ex) {
      //imprime la excepción
      System.out.println(ex.toString());
    } finally {
      //se asegura de cerrar la conexión
      conn.close();
    }

  }

  /****************************************************************************
   * borra todos los objetos que van a crease (si existen)
   *
   * @param conn
   * @throws SQLException
   */
  private void drop_Ejemplo(Connection conn) throws SQLException {

    //consulta SQL
    String consulta = "DROP TABLE IF EXISTS viviendas_alquiler;"
            + "DROP TYPE IF EXISTS vivienda, direccion CASCADE;";

    //comando auxiliar para ejecutar la consulta
    Statement sta = conn.createStatement();

    //ejecuta la consulta
    sta.execute(consulta);

    //cierra el objeto auxiliar
    sta.close();
  }

  /****************************************************************************
   * crea los tipos estructurados 'vivienda' y 'direccion', y la función 
   * 'publicidad' que compone el mensaje publicitario personalizado
   * 
   * @param conn
   * @throws SQLException
   */
  private void crear_Tipos(Connection conn) throws SQLException {
    //Comando auxiliar para ejecutar la consulta
    Statement sta = conn.createStatement();

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //escribe la consulta SQL para construir el tipo 'vivienda', el tipo
    //'direccion', y la función 'publicidad', de acuerdo con las
    //indicaciones dadas
    sta.execute("CREATE TYPE vivienda AS("
            + "planta INTEGER,"
            + "metros_2 INTEGER,"
            + "num_habitaciones INTEGER,"
            + "num_banios INTEGER,"
            + "arrendador VARCHAR(25))");
    
    sta.execute("CREATE TYPE direccion AS("
            + "calle VARCHAR(40),"
            + "ciudad VARCHAR(25),"
            + "provincia VARCHAR(25),"
            + "codigo_postal VARCHAR(5))");

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //crea la función que transforma el tipo estructurado en una cadena
    sta.execute("CREATE FUNCTION publicidad(vivienda) RETURNS varchar AS "
            + "$$SELECT 'Se alquila piso de '||$1.metros_2|| ' metros cuadrados"
            + "en planta '||$1.planta||', con '||$1.num_habitaciones||' habitaciones y '"
            + "|| $1.num_banios||' baños';$$"
            + "LANGUAGE SQL");

    //cierra el objeto auxiliar
    sta.close();
  }

  /****************************************************************************
   * crea la tabla 'viviendas_alquiler'
   * 
   * 
   * @param conn
   * @throws SQLException 
   */
  private void crear_Tabla(Connection conn) throws SQLException {
    //comando auxiliar para ejecutar la consulta
    Statement sta = conn.createStatement();
    
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //escribe la consulta SQL para crear la tabla 'viviendas_alquiler' de acuerdo
    //con las indicaciones dadas
    //ejecuta la consulta
    sta.execute("CREATE TABLE viviendas_alquiler("
            + "vivienda_id serial, "
            + "caracteristicas vivienda, "
            + "ubicacion direccion);");
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //cierra el objeto auxiliar
    sta.close();
  }

  /****************************************************************************
   * inserta unos cuantos registros de ejemplo
   * 
   * @param conn
   * @throws SQLException 
   */
  private void insertar_Registros(Connection conn) throws SQLException {
    //comando auxiliar para ejecutar la consulta
    Statement sta = conn.createStatement();

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //escribe una consulta SQL para insertar al menos 2 registros en la
    //tabla 'viviendas_alquiler', sin dejar ningún valor vacío (excepto el
    //auto-numeríco). Para construir los valores de las columnas de tipos 
    //estructurados emplea la función ROW()
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(1, 80, 3, 2, 'Luisa Manuel Martin'),"
            + "ROW('Calle Blas Infante 3', 'Vva. Rio y Minas', 'Sevilla', '41350'))");
    
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(3, 120, 2, 1, 'Jose Tojeiro Manuel'),"
            + "ROW('Calle Mesones 16', 'Tocina', 'Sevilla', '41340'))");
    
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(5, 110, 4, 1, 'Vicente Andrade Montiel'),"
            + "ROW('Calle Extremadura 1', 'Cantillana', 'Sevilla', '41320'))");
    
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(2, 90, 3, 2, 'Josefa Gomez Grande'),"
            + "ROW('Plaza Chito 4', 'Brenes', 'Sevilla', '41310'))");
    
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(2, 110, 3, 2, 'Maria Andrade Jimenez'),"
            + "ROW('Avenida de Aguas Santas 13', 'Villaverde del Rio', 'Sevilla', '41315'))");
    
    sta.execute("INSERT INTO viviendas_alquiler(caracteristicas, ubicacion) VALUES ("
            + "ROW(2, 50, 1, 1, 'Eulalio Cerezo Gomez'),"
            + "ROW('Calle Tarragona 11', 'Vva. Rio y Minas', 'Sevilla', '41350'))");
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //cierra el objeto auxiliar
    sta.close();
  }

  /****************************************************************************
   * rellena el cuadro combinado con las ciudades donde se ubican las
   * viviendas
   * 
   * @param conn
   * @throws SQLException 
   */
  private void rellenar_JComboBox(Connection conn) throws SQLException {
    //comando auxiliar para ejecutar la consulta
    Statement sta = conn.createStatement();
    
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //escribe una consulta SQL que devuelva todas las ciudades distintas 
    //presentes en el tipo compuesto 'direccion' de la columna 'ubicacion', 
    //ordenadas por nombre
    //ejecuta la consulta para que devuelva un conjunto de registros
    ResultSet res = sta.executeQuery("SELECT DISTINCT (ubicacion).ciudad "
            + "FROM viviendas_alquiler ORDER BY (ubicacion).ciudad ASC");
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //límpia el jComboBox1...
    jComboBox1.removeAllItems();

    //antes de agregar los nuevos registros
    while (res.next()) {
      jComboBox1.addItem(res.getObject(1));
    }

    //cierra los objetos auxiliares
    res.close();
    sta.close();
  }

  /****************************************************************************
   * rellena la tabla con las viviendas de la ciudad especificada sobre una
   * conexión abierta
   * 
   * @param conn
   * @param ciudad
   * @throws SQLException 
   */
  private void rellenar_JTable(Connection conn, Object ciudad) throws SQLException {
    
    if (ciudad != null) {
      //cuadrícula por defecto
      DefaultTableModel modelo = new DefaultTableModel();
      jTable1.setModel(modelo);

      //comando auxiliar para ejecutar la consulta
      Statement sta = conn.createStatement();
      
      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      //escribe una consulta SQL que devuelva todos los datos de las viviendas
      //alquiler ubicadas en la 'ciudad' recibida como parámetro
      ResultSet res = sta.executeQuery("SELECT * FROM viviendas_alquiler "
              + "WHERE (viviendas_alquiler.ubicacion).ciudad='" + ciudad + "'");
      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

      //estructura del conjunto del registros
      ResultSetMetaData rsMd = res.getMetaData();
      int cantidadColumnas = rsMd.getColumnCount();

      //nombres de campo como encabezado de cuadrícula
      for (int i = 1; i <= cantidadColumnas; i++) {
        modelo.addColumn(rsMd.getColumnLabel(i));
      }
      
      //mientras quedan registros por leer
      while (res.next()) {
        //array de objetos
        Object[] fila = new Object[cantidadColumnas];
        
        //rellena los datos de la fila
        for (int i = 0; i < cantidadColumnas; i++) {
          fila[i] = res.getObject(i + 1);
        }
        
        //agrega el array a la cuadrícula
        modelo.addRow(fila);
      }

      //cierra los objetos auxiliares
      res.close();
      sta.close();
    }
  }

  /****************************************************************************
   * abre una conexión y se la pasa al método rellenar_JTable() de más arriba,
   * para rellenar la tabla con las viviendas de la ciudad especificada
   * 
   * @param ciudad
   * @throws SQLException 
   */
  private void rellenar_JTable(Object ciudad) throws SQLException {

    //conexión con la base de datos
    Connection conn = null;

    try {
      //abre la conexión con la base de datos a la que apunta el url
      //mediante la contraseña del usuario postgres
      conn = DriverManager.getConnection(url, "postgres", passwd);

      //método que rellena la tabla a partir de la conexión y la ciudad
      rellenar_JTable(conn, ciudad);

    } catch (SQLException ex) {
      //imprime la excepción
      System.out.println(ex.toString());
    } finally {

      //cierra la conexión
      conn.close();
    }
  }

  /****************************************************************************
   * compone el mensaje publicitario para la vivienda de clave especificada
   * 
   * @param viviendaId
   * @throws SQLException 
   */
  private void mensajePublicitario(Object viviendaId) throws SQLException {

    //conexión con la base de datos
    Connection conn = null;

    try {
      //abre la conexión con la base de datos a la que apunta el url
      //mediante la contraseña del usuario postgres
      conn = DriverManager.getConnection(url, "postgres", passwd);

      //comando auxiliar para ejecutar la consulta
      Statement sta = conn.createStatement();
      
      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      //escribe una consulta SQL que devuelva el valor de la función
      //'publicidad', sobre la columna 'caracteristicas' de la vivienda alquiler
      //de clave principal recibida como parámetro
      ResultSet res = sta.executeQuery("SELECT publicidad(caracteristicas)"
              + "FROM viviendas_alquiler WHERE vivienda_id = '" + viviendaId + "'");
      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      
      //muestra el resultado
      while (res.next()) {
        jTextField1.setText(res.getString(1));
      }

      //cierra el objeto auxiliar
      sta.close();

    } catch (SQLException ex) {
      //imprime la excepción
      System.out.println(ex.toString());
    } finally {
      //cierra la conexión
      conn.close();
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Oferta de vivienda alquiler en la ciudad de:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setFocusable(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Para la vivienda seleccionada...");

        jButton1.setText("Consultar el valor devuelto por la función 'publicidad' sobre el tipo 'vivienda' de la columna 'caracteristicas'");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  /****************************************************************************
   * responde a la pulsación del botón
   * 
   * @param evt 
   */
  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    
    //índice de la fila seleccionada en la cuadrícula
    int filaSeleccionada = jTable1.getSelectedRow();
    
    //variable para la clave principal de una vivienda (iniciada en 0). Aunque
    //se trata de un tipo entero, se utiliza un Object para evitar conversiones
    //de tipos posteriores
    Object viviendaId;

    try {
      //si hay alguna fila seleccionada en la cuadrícula
      if (filaSeleccionada > -1) {
        //anota su clave principal (almacenada en la columna de índice 0)
        viviendaId = jTable1.getValueAt(filaSeleccionada, 0);
        
        //llama al método mensajePublicitario con la clave anotada
        mensajePublicitario(viviendaId);
      } else {
        //borra el mensaje publicitario
        jTextField1.setText(null);
        
        //muestra un mensaje emerjente
        JOptionPane.showMessageDialog(null, "¡Seleccione una vivienda!");
      }
      
    } catch (SQLException ex) {
      //depura la excepción
      Logger.getLogger(jfMain.class.getName()).log(Level.SEVERE, null, ex);
    }
  }//GEN-LAST:event_jButton1ActionPerformed

  /****************************************************************************
   * responde al cambio de elemento en el ComboBox
   * 
   * @param evt 
   */
  private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

    //anota el nombre de la ciudad como Objeto para evitar conversiones
    Object nombreCiudad = jComboBox1.getSelectedItem();
    
    try {
      //borra el mensaje publicitario
      jTextField1.setText(null);
      rellenar_JTable(nombreCiudad);

    } catch (SQLException ex) {
      //trata la excepción
      Logger.getLogger(jfMain.class.getName()).log(Level.SEVERE, null, ex);
    }
  }//GEN-LAST:event_jComboBox1ItemStateChanged

  /****************************************************************************
   * responde al clic sobre la cuadrícula
   * 
   * @param evt 
   */
  private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    
    //si clic con el botón principal
    if (evt.getButton() == MouseEvent.BUTTON1) {
      //borra el mensaje publicitario
      jTextField1.setText(null);
    }
  }//GEN-LAST:event_jTable1MouseClicked

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(jfMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(jfMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(jfMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(jfMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        try {
          new jfMain().setVisible(true);
        } catch (SQLException ex) {
          Logger.getLogger(jfMain.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
