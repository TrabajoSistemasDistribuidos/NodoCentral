# Nodo Central
El proyecto del Nodo Central es responsable de distribuir los números aleatorios a los nodos trabajadores y de recolectar los resultados para determinar cuántos de esos números son primos. Utiliza ZeroMQ para la comunicación entre el nodo central y los nodos trabajadores. El flujo de trabajo es el siguiente:  
 - Genera una lista de 1000 números aleatorios.
 - Divide la lista en partes iguales y envía cada parte a uno de los nodos trabajadores utilizando un socket PUSH.
 - Recibe los resultados de los nodos trabajadores a través de un socket PULL.
 - Cuenta cuántos de los números recibidos son primos y muestra el total.
