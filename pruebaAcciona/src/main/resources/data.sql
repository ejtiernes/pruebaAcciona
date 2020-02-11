INSERT INTO 
	TBL_TWITER (usuario, texto, localizacion,validacion) 
VALUES
  	('Lokesh', 'a diam #maecenas #sed enim ut #motogp sem viverra aliquet eget #sit amet', 'howtodoinjava@gmail.com', false),
  	('Lokesh', 'a diam maecenas #sed enim ut sem #motogp viverra aliquet eget #sit #amet', 'howtodoinjava@gmail.com', false),
  	('Lokesh', 'a diam #maecenas sed #enim #ut sem viverra #motogp aliquet #eget sit #amet', 'howtodoinjava@gmail.com', true),
  	('Lokesh', 'a diam maecenas sed #maecenas #enim ut sem viverra #motogp aliquet #eget sit amet', 'howtodoinjava@gmail.com', false),
  	('Lokesh', '#a #diam #maecenas #sed #enim #ut #sem #viverra #aliquet #eget #sit #motogp #amet', 'howtodoinjava@gmail.com', false),
  	('John', 'Doe', 'xyz@email.com', false);