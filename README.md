# Dating App Project

## Functionality

Planot mi e da ima poveke korisnici i koga sekoj korisnik se registrira na stranata toj e vidliv za site i tie mozat da gi stavat vo lista (wishlist), ako
se zainteresirani za niv, isto taka ima i opcija da im se dopagaat i ako dvajcata odberat opcijata deka se dopagaat megu sebe, togas tie ke bidat vo drug del
(matched) i planiram tie sto se matched da dobijat opcija da si prakaat poraki (ako uspeam da go napravam toa), ako ne uspeam so poraki togas namesto poraki,
samo bi go pokazal nivniot telefonski broj, pa preku nego mozat da si pisuvaat. Isto bi mozel da napravam kako timeline kade sekoj user moze da pravi postovi,
no i ova ne znam kolku ke bide lesno.

![](../../planning/Dating%20app.png)

## Classes
User: 
String(name, surname, username, password, bio, city, gender);
Int(Age);
List<User> interested(Many to many);
List<User> likedBy(Many to many);
List<User> matched(Many to many);
List<Message> messages(Many to many);
List<Post> posts(OnetoMany);

Post: 
String(content);
User poster(Many to one);

Message:
String(content);
User from(Many to one);
User to(Many to one):