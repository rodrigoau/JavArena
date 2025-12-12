package com.rodrigo.datastructures;

import java.util.ArrayList;
import java.util.List;

public class JavaCollections {

    public static void main(String[] args) {

        /**
         * ------------------------------------------------------------------------------------------------------------------
         * RESUMEN DE JAVA COLLECTIONS
         * ------------------------------------------------------------------------------------------------------------------
         * | TIPO      | INTERFAZ | IMPLEMENTACIÓN | CARACTERÍSTICA PRINCIPAL              | EJEMPLO REAL                   |
         * |-----------|----------|----------------|---------------------------------------|--------------------------------|
         * | Lista     | List     | ArrayList      | Ordenada, permite duplicados.         | Lista de canciones en Spotify. |
         * | Conjunto  | Set      | HashSet        | Desordenada, NO permite duplicados.   | Emails únicos en una BD.       |
         * | Mapa      | Map      | HashMap        | Parejas Clave-Valor.                  | Diccionario (Palabra -> Sig).  |
         * ------------------------------------------------------------------------------------------------------------------
         *
         * | TIPO      | INTERFAZ | IMPLEMENTACIÓN | CARACTERÍSTICA PRINCIPAL              | EJEMPLO REAL                   |
         * |-----------|----------|----------------|---------------------------------------|--------------------------------|
         * | ...       | ...      | ...            | ...                                   | ...                            |
         * | Conjunto  | Set      | TreeSet        | Ordenado (A-Z, 0-9), único.           | Lista de asistencia alfabética.|
         * | Conjunto  | Set      | LinkedHashSet  | Orden de llegada, único.              | Historial de acciones (undo).  |
         */

        List list = new ArrayList();
        list.add(1);
        list.add("2");
        list.add(3.0);

        list.forEach( e -> {
            System.out.println(e);
        });

        List<String> names = new ArrayList<>();
        names.add("sky");
        names.add("snow");
        names.add("flower");

        System.out.println(names);
        System.out.println(names.get(1));


    }

}
