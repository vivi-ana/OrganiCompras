package acostapeter.com.organicompras.data;

class DbTablas {

    static class TablaCompras {
        static final String TABLA_COMPRAS = "compras";
        static final String CAMPO_ID_COMPRA = "id_compra";
        static final String CAMPO_FK_ID_SUPER = "id_supermercado";
        static final String CAMPO_FECHA = "fecha";
        static final String CAMPO_MAX = "max";
        static final String CAMPO_CANT_PROD = "cant_prod";
        static final String CAMPO_TOTAL = "total";
        static final String CAMPO_TOT_UNITARIO = "totalunitario";
    }
    static class TablaDetallesCompras {
        static final String TABLA_DETALLE = "detalle_compras";
        static final String CAMPO_ID_DETALLE = "id_detalle";
        static final String CAMPO_FK_ID_COMPRA = "id_compra";
        static final String CAMPO_FK_ID_PROD = "id_producto";
        static final String CAMPO_CANTIDAD = "cantidad";
        static final String CAMPO_MONTO = "monto";
    }
    static class TablaProductos {
        static final String TABLA_PRODUCTOS= "productos";
        static final String CAMPO_ID_PROD = "id_producto";
        static final String CAMPO_NOMBRE = "id_detalle_producto";
        static final String CAMPO_DESCP = "descripcion";
        static final String CAMPO_MARCA = "marca";
        static final String CAMPO_CONT_NETO = "cont_neto";
        static final String CAMPO_MEDIDA = "medida";
    }
    static class TablaInventarios {
        static final String TABLA_INVENTARIOS = "inventarios";
        static final String CAMPO_FK_ID_PROD = "id_producto";
        static final String CAMPO_CANT = "cantidad";
        static final String CAMPO_GUARDAR = "guardar";
    }
    static class TablaSupermercados {
        static final String TABLA_SUPER = "supermercados";
        static final String CAMPO_ID_SUPER = "id_supermercado";
        static final String CAMPO_DESCRIP = "descripcion";
    }
    static class TablaProdXSuper {
        static final String TABLA_PRODXSUPER = "prodxsuper";
        static final String CAMPO_FK_ID_PROD = "id_producto";
        static final String CAMPO_FK_ID_SUPER = "id_supermercado";
        static final String CAMPO_PRECIO = "precio";
    }
    static class TablaDetallesProd {
        static final String TABLA_DETALLE_PROD = "detalle_producto";
        static final String CAMPO_ID_PROD = "id_detalle_producto";
        static final String CAMPO_NOMBRE = "nombre";
    }
    static class TablaProdNoEncoDespensa {
        static final String TABLA_PROD_NO_EN_DESP = "producto_no_encontrado";
        static final String CAMPO_ID_NO_EN = "id_no_producto";
        static final String CAMPO_NOMBRE = "nombre";
        static final String CAMPO_GUARDAR = "guardar";
    }
    static class TablaProdNoEncoCompras {
        static final String TABLA_PROD_NO_EN_COMP = "prod_no_encontrado";
        static final String CAMPO_ID_NO_EN = "id_no_encontrado";
        static final String CAMPO_FK_ID_SUPER = "id_supermercado";
        static final String CAMPO_NOMBRE = "nombre";
        static final String CAMPO_PRECIO = "precio";
    }

    //SENTENCIA PARA CREAR TABLA COMPRAS
    final String COMPRAS_CREATE = "create table " + TablaCompras.TABLA_COMPRAS + " ( "
            + TablaCompras.CAMPO_ID_COMPRA + " integer primary key autoincrement, "
            + TablaCompras.CAMPO_FK_ID_SUPER + " integer not null , "
            + TablaCompras.CAMPO_FECHA + " date not null , "
            + TablaCompras.CAMPO_MAX + " real not null , "
            + TablaCompras.CAMPO_CANT_PROD + " integer not null , "
            + TablaCompras.CAMPO_TOTAL + " real not null , "
            + TablaCompras.CAMPO_TOT_UNITARIO + " real not null ) ; ";

    final String DETALLES_CREATE = "create table " + TablaDetallesCompras.TABLA_DETALLE + " ( "
            + TablaDetallesCompras.CAMPO_ID_DETALLE + " integer primary key autoincrement, "
            + TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " integer not null , "
            + TablaDetallesCompras.CAMPO_FK_ID_PROD + " real not null , "
            + TablaDetallesCompras.CAMPO_CANTIDAD + " integer not null , "
            + TablaDetallesCompras.CAMPO_MONTO + " real not null ) ; ";

    final String PROD_CREATE = "create table " + TablaProductos.TABLA_PRODUCTOS + " ( "
            + TablaProductos.CAMPO_ID_PROD + " real primary key, "
            + TablaProductos.CAMPO_NOMBRE + " integer not null , "
            + TablaProductos.CAMPO_DESCP + " text not null , "
            + TablaProductos.CAMPO_MARCA + " text not null , "
            + TablaProductos.CAMPO_CONT_NETO + " real not null , "
            + TablaProductos.CAMPO_MEDIDA + " text not null ) ; ";

    final String INVENTARIO_CREATE = "create table " + TablaInventarios.TABLA_INVENTARIOS + " ( "
            + TablaInventarios.CAMPO_FK_ID_PROD + " text not null, "
            + TablaInventarios.CAMPO_CANT + " integer not null , "
            + TablaInventarios.CAMPO_GUARDAR + " text not null ) ; ";

    final String SUPER_CREATE = "create table " + TablaSupermercados.TABLA_SUPER + " ( "
            + TablaSupermercados.CAMPO_ID_SUPER + " integer primary key autoincrement, "
            + TablaSupermercados.CAMPO_DESCRIP + " text not null ) ; ";

    final String PRODXSUPER_CREATE = "create table " + TablaProdXSuper.TABLA_PRODXSUPER + " ( "
            + TablaProdXSuper.CAMPO_FK_ID_PROD + " real not null, "
            + TablaProdXSuper.CAMPO_FK_ID_SUPER + " integer not null , "
            + TablaProdXSuper.CAMPO_PRECIO + " real not null ) ; ";

    final String DETALLEPROD_CREATE = "create table " + TablaDetallesProd.TABLA_DETALLE_PROD + " ( "
            + TablaDetallesProd.CAMPO_ID_PROD + " integer primary key autoincrement, "
            + TablaDetallesProd.CAMPO_NOMBRE + " text not null ) ; ";

    final String DESPENSANOPROD_CREATE = "create table " + TablaProdNoEncoDespensa.TABLA_PROD_NO_EN_DESP + " ( "
            + TablaProdNoEncoDespensa.CAMPO_ID_NO_EN + " integer primary key autoincrement, "
            + TablaProdNoEncoDespensa.CAMPO_NOMBRE + " text not null , "
            + TablaProdNoEncoDespensa.CAMPO_GUARDAR + " text not null ) ; ";

    final String SUPERNOPROD_CREATE = "create table " + TablaProdNoEncoCompras.TABLA_PROD_NO_EN_COMP + " ( "
            + TablaProdNoEncoCompras.CAMPO_ID_NO_EN + " real primary key, "
            + TablaProdNoEncoCompras.CAMPO_FK_ID_SUPER + " integer not null , "
            + TablaProdNoEncoCompras.CAMPO_NOMBRE + " text not null , "
            + TablaProdNoEncoCompras.CAMPO_PRECIO + " real not null ) ; ";

}
