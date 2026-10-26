package com.rodrigo.core.datastructures;

import java.util.*;


/*
 * --------------------------------------------------------------------------------------------------------
 *  RESUMEN DE JAVA COLLECTIONS
 * --------------------------------------------------------------------------------------------------------
 *
 *  > List<E>  — Secuencia ordenada por índice. Permite duplicados.
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | IMPLEMENTACIÓN | ORDEN         | DUPLICADOS | NULL    | ACCESO      | EJEMPLO REAL                   |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | ArrayList      | Índice        | Sí         | Sí      | get(i) O(1) | Playlist de Spotify            |
 * | LinkedList     | Índice        | Sí         | Sí      | get(i) O(n) | Cola de impresión              |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 *
 *  > Set<E>  — Elementos únicos (usa equals/hashCode o compareTo).
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | IMPLEMENTACIÓN | ORDEN         | DUPLICADOS | NULL    | CONTAINS    | EJEMPLO REAL                   |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | HashSet        | Ninguno       | No         | 1 null  | O(1)        | Emails únicos en una BD        |
 * | LinkedHashSet  | Inserción     | No         | 1 null  | O(1)        | Tags en orden de captura       |
 * | TreeSet        | Natural (A-Z) | No         | No      | O(log n)    | Lista de asistencia alfabética |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 *
 *  > Map<K,V>  — Parejas clave-valor. Claves únicas, valores pueden repetirse.
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | IMPLEMENTACIÓN | ORDEN         | DUPLICADOS | NULL    | GET(KEY)    | EJEMPLO REAL                   |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 * | HashMap        | Ninguno       | Claves no  | 1 clave | O(1)        | Caché de sesión (id -> user)   |
 * | LinkedHashMap  | Inserción     | Claves no  | 1 clave | O(1)        | Caché LRU                      |
 * | TreeMap        | Por clave     | Claves no  | No      | O(log n)    | Diccionario (palabra -> def.)  |
 * |----------------|---------------|------------|---------|-------------|--------------------------------|
 *
 *  NOTAS:
 *   - Declara con la interfaz, instancia con la implementación:
 *       List<String> canciones = new ArrayList<>();
 *   - Map NO extiende Collection (List y Set sí).
 *   - TreeSet/TreeMap ordenan por orden natural o por un Comparator propio.
 *   - Ninguna de estas es thread-safe; para concurrencia: ConcurrentHashMap, CopyOnWriteArrayList.
 * --------------------------------------------------------------------------------------------------------
 */

public class CollectionsDemo {
    public static void main(String[] args) {
        imprimir(1);
        imprimir("hola");
        String[] nr = { "a", "b" };
        for (String n:nr){
            imprimir(n);
        }
    }


    /** ArrayList: acceso rápido por índice. Ideal cuando lees más de lo que insertas/borras al inicio. */
    static void arrayListDemo(){
        List<String> playlist = new ArrayList<>();
        playlist.add("Bohemian Rhapsody");
        playlist.add("Hotel California");
        playlist.add("Bohemian Rhapsody");
        playlist.add(null);

        System.out.println(playlist);
        System.out.println(playlist.get(0));
        System.out.println(playlist.size());
    }

    /** LinkedList: insertar/quitar en los extremos es O(1). También funciona como cola (Queue). */
    static void linkedListDemo(){
        LinkedList<String> colaImpresion = new LinkedList<>();
        colaImpresion.add("factura.pdf");
        colaImpresion.add("contrato.docx");
        colaImpresion.addFirst("URGENTE.pdf");

        System.out.println(colaImpresion);
        System.out.println("Imprimiendo... " + colaImpresion.poll());
        System.out.println("Pendientes... " + colaImpresion);
    }

    /** HashSet: unicidad con búsqueda O(1). No garantiza ningún orden. */
    static void hashSetDemo(){
        Set<String> emails = new HashSet<>();
        System.out.println("Registrando emails:");
        System.out.println(" add ana@email.com -> " + emails.add("ana@email.com"));
        System.out.println(" add luis@email.com -> " + emails.add("luis@email.com"));
        System.out.println(" add ana@email.com -> " + emails.add("ana@email.com") + " (duplicado rechazado)");
        System.out.println(" ¿Existe luis@email.com? " + emails.contains("luis@email.com"));
        System.out.println(" Emails (Orden no garantizado): " + emails);
    }

    /** LinkedHashSet: únicos + conserva el orden en que se insertaron. */
    static void linkedHashSetDemo(){
        Set<String> tags = new LinkedHashSet<>();
        tags.add("playa");
        tags.add("yate");
        tags.add("atardecer");
        tags.add("playa");

        System.out.println(" Tags en orden de captura " + tags);
    }

    /** TreeSet: únicos + siempre ordenados (orden natural o Comparator). */
    static void treeSetDemo(){
        Set<String> asistencia = new TreeSet<>();
        asistencia.add("Zamora");
        asistencia.add("Álvarez");
        asistencia.add("Martínez");
        asistencia.add("Benítez");
        asistencia.add("Martínez");

        System.out.println("Lista de asistencia: " + asistencia);
        System.out.println("Ojo: 'Álvarez' queda al final porque compara por código Unicode, no por idioma.");

        TreeSet<Integer> calificaciones = new TreeSet<>(Comparator.reverseOrder());
        calificaciones.addAll(List.of(85,92, 70, 100, 45));
        System.out.println(" Calificaciones únicas (desc): " + calificaciones);
        System.out.println(" Mejor calificacion: " + calificaciones.first());

    }

    // ------------------------------------------------------------------
    // MAP: parejas clave -> valor, claves únicas
    // ------------------------------------------------------------------

    /** HashMap: buscar un valor por su clave en O(1). Sin orden. */
    static void hashMapDemo(){
        Map<String, String> sesiones = new HashMap<>();
        sesiones.put("tok-123", "rodrigo");
        sesiones.put("tok-456", "ana");
        sesiones.put("tok-789", "ana");

        sesiones.put("tok-123", "rodrigo_v2");

        System.out.println("Sesiones: " + sesiones);
        System.out.println("usuario tok-789: " + sesiones.get("tok-789"));
        System.out.println("usuario tok-000: " + sesiones.getOrDefault("tok-000", "ánonimo"));

        System.out.println("Sesiones: " + sesiones);
    }

    /** LinkedHashMap: conserva orden de inserción, o de acceso (útil para una caché LRU). */
    static void linkedHashMapDemo(){
        final int CAPACIDAD = 3;
        Map<String, String> cacheLRU = new LinkedHashMap<>(16, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
                return size() > CAPACIDAD;
            }
        };

        cacheLRU.put("/home", "<html>home</html>");
        cacheLRU.put("/hoteles", "<html>hoteles</html>");
        cacheLRU.put("/yates", "<html>yates</html>");
        cacheLRU.get("/home");

        cacheLRU.put("/contacto", "<html>contacto</html>");
        System.out.println("Cache LRU (de menos a mas reciente): " + cacheLRU.keySet());
    }

    /** TreeMap: claves siempre ordenadas + búsquedas por rango. */
    static void treeMapDemo() {
        TreeMap<String, String> diccionario = new TreeMap<>();
        diccionario.put("velero", "Embarcación impulsada por el viento");
        diccionario.put("ancla", "Pieza para fijar una embarcación al fondo");
        diccionario.put("muelle", "Construcción para atracar barcos");
        diccionario.put("catamarán", "Embarcación de dos cascos");

        System.out.println("\n[TreeMap] Diccionario ordenado:");
        diccionario.forEach((palabra, def) -> System.out.println("  " + palabra + " -> " + def));

        System.out.println("  Primera palabra: " + diccionario.firstKey());
        System.out.println("  Palabras de 'c' a 'n': " + diccionario.subMap("c", "n").keySet());
    }

    static void workshop(){
    }


    static <T> T imprimir(T value){
        System.out.println(value);
        return value;
    }


}
