INSERT INTO autores (id, nombre, alta, baja, id_foto) VALUES("3ddd8dc2-d4c1-11ee-8549-d85ed3dfffce",'Herman Melville',NOW(),NULL, NULL);
INSERT INTO autores (id, nombre, alta, baja, id_foto) VALUES("3ddde80e-d4c1-11ee-8549-d85ed3dfffce",'Harper Lee',NOW(),NULL, NULL);
INSERT INTO autores (id, nombre, alta, baja, id_foto) VALUES("3dde3c30-d4c1-11ee-8549-d85ed3dfffce",'F. Scott Fitzgerald',NOW(),NULL, NULL);
INSERT INTO autores (id, nombre, alta, baja, id_foto) VALUES("3dde9186-d4c1-11ee-8549-d85ed3dfffce",'George Orwell',NOW(),NULL, NULL);
INSERT INTO autores (id, nombre, alta, baja, id_foto) VALUES("3ddeef10-d4c1-11ee-8549-d85ed3dfffce",'Miguel de Cervantes',NOW(),NULL, NULL);

INSERT INTO editoriales (id, nombre, alta, baja, id_foto) VALUES ("3ddf63b8-d4c1-11ee-8549-d85ed3dfffce",'Penguin Classics',NOW(),NULL, NULL);
INSERT INTO editoriales (id, nombre, alta, baja, id_foto) VALUES ("3ddfc70f-d4c1-11ee-8549-d85ed3dfffce",'Harper Perennial Modern Classics',NOW(),NULL, NULL);
INSERT INTO editoriales (id, nombre, alta, baja, id_foto) VALUES ("3de02f3f-d4c1-11ee-8549-d85ed3dfffce",'Scribner',NOW(),NULL, NULL);
INSERT INTO editoriales (id, nombre, alta, baja, id_foto) VALUES ("3de08ae9-d4c1-11ee-8549-d85ed3dfffce",'Signet Classics',NOW(),NULL, NULL);
INSERT INTO editoriales (id, nombre, alta, baja, id_foto) VALUES ("3df05ae5-d4c1-11ee-8549-d85ed3dfffce",'Planeta',NOW(),NULL, NULL);

INSERT INTO libros (id, isbn, titulo, descripcion, fecha_publicacion, cantidad_paginas, cantidad_copias, cantidad_copias_prestadas, cantidad_copias_restantes, alta, baja, id_autor, id_editorial, id_foto) VALUES (UUID(), '9780141439846', 'Moby Dick', 'Novela sobre la obsesión de un capitán de barco por cazar una ballena blanca', '1851-10-18', 624, 100, 20, 80, NOW(), NULL, "3ddd8dc2-d4c1-11ee-8549-d85ed3dfffce", "3ddf63b8-d4c1-11ee-8549-d85ed3dfffce", NULL);
INSERT INTO libros (id, isbn, titulo, descripcion, fecha_publicacion, cantidad_paginas, cantidad_copias, cantidad_copias_prestadas, cantidad_copias_restantes, alta, baja, id_autor, id_editorial, id_foto) VALUES (UUID(), '9780061120084', 'To Kill a Mockingbird', 'Clásico de la literatura estadounidense que trata temas de justicia y moral', '1960-07-11', 336, 150, 30, 120, NOW(), NULL, "3ddde80e-d4c1-11ee-8549-d85ed3dfffce", "3ddfc70f-d4c1-11ee-8549-d85ed3dfffce", NULL);
INSERT INTO libros (id, isbn, titulo, descripcion, fecha_publicacion, cantidad_paginas, cantidad_copias, cantidad_copias_prestadas, cantidad_copias_restantes, alta, baja, id_autor, id_editorial, id_foto) VALUES (UUID(), '9780743273565', 'The Great Gatsby', 'Historia de amor y decadencia en la era del jazz en Estados Unidos', '1925-04-10', 180, 200, 50, 150, NOW(), NULL, "3dde3c30-d4c1-11ee-8549-d85ed3dfffce", "3de02f3f-d4c1-11ee-8549-d85ed3dfffce", NULL);
INSERT INTO libros (id, isbn, titulo, descripcion, fecha_publicacion, cantidad_paginas, cantidad_copias, cantidad_copias_prestadas, cantidad_copias_restantes, alta, baja, id_autor, id_editorial, id_foto) VALUES (UUID(), '9780451524935', '1984', 'Novela distópica que presenta una visión sombría del futuro', '1949-06-08', 328, 120, 10, 110, NOW(), NULL, "3dde9186-d4c1-11ee-8549-d85ed3dfffce", "3de08ae9-d4c1-11ee-8549-d85ed3dfffce", NULL);
INSERT INTO libros (id, isbn, titulo, descripcion, fecha_publicacion, cantidad_paginas, cantidad_copias, cantidad_copias_prestadas, cantidad_copias_restantes, alta, baja, id_autor, id_editorial, id_foto) VALUES (UUID(), '9780140449297', 'Don Quijote', 'Clásico de la literatura española que narra las aventuras de un hidalgo enloquecido', '1605-01-16', 1056, 80, 5, 75, NOW(), NULL, "3ddeef10-d4c1-11ee-8549-d85ed3dfffce", "3df05ae5-d4c1-11ee-8549-d85ed3dfffce", NULL);

INSERT INTO ciudades (id, nombre) VALUES (1, 'Argentina');
INSERT INTO ciudades (id, nombre) VALUES (2, 'París');
INSERT INTO ciudades (id, nombre) VALUES (3, 'Tokio');
INSERT INTO ciudades (id, nombre) VALUES (4, 'Londres');
INSERT INTO ciudades (id, nombre) VALUES (5, 'Roma');
INSERT INTO ciudades (id, nombre) VALUES (6, 'Sídney');
INSERT INTO ciudades (id, nombre) VALUES (7, 'Ciudad de México');
INSERT INTO ciudades (id, nombre) VALUES (8, 'Río de Janeiro');
INSERT INTO ciudades (id, nombre) VALUES (9, 'Nueva York');
INSERT INTO ciudades (id, nombre) VALUES (10, 'Moscú');

INSERT INTO usuarios (id, nombre, apellido, documento, telefono, genero, rol, id_ciudad, id_foto, email, contraseña, alta, baja) VALUES ("55fe85dd-8980-480d-b636-fef5f46aa989" , "Lucas", "Aramberry",  "12345678", "2616622238", "MASCULINO", "ADMIN", 1 ,NULL, "aramberry@lucas.com", "$2a$10$rGGZVXuJ.8rGOcbrI4uHRO88aPDeusZmYIB4fljFtHBdcUbyIJT0q", "2024-03-03 16:59:36", NULL)
INSERT INTO usuarios (id, nombre, apellido, documento, telefono, genero, rol, id_ciudad, id_foto, email, contraseña, alta, baja) VALUES ("55fe85ee-8580-460d-b636-fef5f46aa989" , "Lucas", "Aramberry",  "87654321", "2616622238", "MASCULINO", "USER", 1 ,NULL, "lucas@aramberry.com", "$2a$10$rGGZVXuJ.8rGOcbrI4uHRO88aPDeusZmYIB4fljFtHBdcUbyIJT0q", "2024-03-03 16:59:36", NULL)