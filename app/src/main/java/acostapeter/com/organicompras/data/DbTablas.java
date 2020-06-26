package acostapeter.com.organicompras.data;

class DbTablas {

    static class TablaCompras {
        static final String TABLA_COMPRAS = "COMPRAS";
        static final String CAMPO_ID_COMPRA = "id_compra";
        static final String CAMPO_FK_ID_SUPER = "supermercado_id";
        static final String CAMPO_FECHA = "fecha";
        static final String CAMPO_MAX = "max";
        static final String CAMPO_CANT_PROD = "cant_prod";
        static final String CAMPO_TOTAL = "total";
        static final String CAMPO_TOT_UNITARIO = "total_unitario";
    }
    static class TablaDetallesCompras {
        static final String TABLA_DETALLES = "DETALLES_COMPRAS";
        static final String CAMPO_ID_DETALLE = "id_detalle";
        static final String CAMPO_FK_ID_COMPRA = "compra_id";
        static final String CAMPO_FK_ID_PROD = "producto_id";
        static final String CAMPO_CANTIDAD = "cantidad";
        static final String CAMPO_MONTO = "monto";
    }
    static class TablaProductos {
        static final String TABLA_PRODUCTOS= "PRODUCTOS";
        static final String CAMPO_ID_PROD = "id_producto";
        static final String CAMPO_NOMBRE = "nombre";
        static final String CAMPO_DESCP = "descripcion";
        static final String CAMPO_MARCA = "marca";
        static final String CAMPO_CONT_NETO = "cont_neto";
        static final String CAMPO_MEDIDA = "medida";
    }
    static class TablaInventarios {
        static final String TABLA_INVENTARIOS = "INVENTARIOS";
        static final String CAMPO_FK_ID_PROD = "producto_id";
        static final String CAMPO_CANT = "cantidad";
        static final String CAMPO_GUARDAR = "guardar";
    }
    static class TablaSupermercados {
        static final String TABLA_SUPERMERCADOS = "SUPERMERCADOS";
        static final String CAMPO_ID_SUPERMERCADO = "id_supermercado";
        static final String CAMPO_NOMBRE = "nombre";
    }
    static class TablaProdXSuper {
        static final String TABLA_PRODXSUPER = "PRODXSUPER";
        static final String CAMPO_FK_ID_EXISTENTE = "existente_id";
        static final String CAMPO_FK_ID_SUPER = "supermercado_id";
        static final String CAMPO_PRECIO = "precio";
    }
    static class TablaExistentes {
        static final String TABLA_EXISTENTES = "PRODUCTOS_EXISTENTES";
        static final String CAMPO_ID_EXISTENTE = "id_existente";
        static final String CAMPO_FK_ID_PROD = "producto_id";
    }
    static class TablaProductosCasa {
        static final String TABLA_PROD_CASA = "PRODUCTOS_CASA";
        static final String CAMPO_ID_CASA = "id_producto_casa";
        static final String CAMPO_FK_ID_PROD = "producto_id";
        static final String CAMPO_GUARDAR = "guardar";
    }
    static class TablaProductosSuper {
        static final String TABLA_PROD_NO_EN_COMP = "PRODUCTOS_SUPER";
        static final String CAMPO_ID_PROD_SUPER = "id_producto_super";
        static final String CAMPO_FK_ID_PROD = "producto_id";
        static final String CAMPO_FK_ID_SUPER = "supermercado_id";
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

    final String DETALLES_CREATE = "create table " + TablaDetallesCompras.TABLA_DETALLES + " ( "
            + TablaDetallesCompras.CAMPO_ID_DETALLE + " integer primary key autoincrement , "
            + TablaDetallesCompras.CAMPO_FK_ID_COMPRA + " integer not null , "
            + TablaDetallesCompras.CAMPO_FK_ID_PROD + " integer not null , "
            + TablaDetallesCompras.CAMPO_CANTIDAD + " integer not null , "
            + TablaDetallesCompras.CAMPO_MONTO + " real not null ) ; ";

    final String PROD_CREATE = "create table " + TablaProductos.TABLA_PRODUCTOS + " ( "
            + TablaProductos.CAMPO_ID_PROD + " integer primary key autoincrement, "
            + TablaProductos.CAMPO_NOMBRE + " text not null , "
            + TablaProductos.CAMPO_DESCP + " text , "
            + TablaProductos.CAMPO_MARCA + " text , "
            + TablaProductos.CAMPO_CONT_NETO + " real , "
            + TablaProductos.CAMPO_MEDIDA + " text ) ; ";

    final String INVENTARIO_CREATE = "create table " + TablaInventarios.TABLA_INVENTARIOS + " ( "
            + TablaInventarios.CAMPO_FK_ID_PROD + " integer not null, "
            + TablaInventarios.CAMPO_CANT + " integer not null , "
            + TablaInventarios.CAMPO_GUARDAR + " text not null ) ; ";

    final String SUPER_CREATE = "create table " + TablaSupermercados.TABLA_SUPERMERCADOS + " ( "
            + TablaSupermercados.CAMPO_ID_SUPERMERCADO + " integer primary key autoincrement, "
            + TablaSupermercados.CAMPO_NOMBRE + " text not null ) ; ";

    final String PRODXSUPER_CREATE = "create table " + TablaProdXSuper.TABLA_PRODXSUPER + " ( "
            + TablaProdXSuper.CAMPO_FK_ID_EXISTENTE + " text not null, "
            + TablaProdXSuper.CAMPO_FK_ID_SUPER + " integer not null , "
            + TablaProdXSuper.CAMPO_PRECIO + " real not null ) ; ";

    final String EXISTENTES_CREATE = "create table " + TablaExistentes.TABLA_EXISTENTES + " ( "
            + TablaExistentes.CAMPO_ID_EXISTENTE + " text not null, "
            + TablaExistentes.CAMPO_FK_ID_PROD + " integer not null ) ; ";

    final String DESPENSANOPROD_CREATE = "create table " + TablaProductosCasa.TABLA_PROD_CASA + " ( "
            + TablaProductosCasa.CAMPO_ID_CASA + " text not null, "
            + TablaProductosCasa.CAMPO_FK_ID_PROD + " integer not null , "
            + TablaProductosCasa.CAMPO_GUARDAR + " text not null ) ; ";

    final String SUPERNOPROD_CREATE = "create table " + TablaProductosSuper.TABLA_PROD_NO_EN_COMP + " ( "
            + TablaProductosSuper.CAMPO_ID_PROD_SUPER + " text not null, "
            + TablaProductosSuper.CAMPO_FK_ID_SUPER + " integer not null , "
            + TablaProductosSuper.CAMPO_FK_ID_PROD + " integer not null , "
            + TablaProductosSuper.CAMPO_PRECIO + " real not null ) ; ";

}
