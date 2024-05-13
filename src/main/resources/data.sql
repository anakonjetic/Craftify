insert into category(name) values('Home Decor');
insert into category(name) values('Upcycling/Recycling');
insert into category(name) values('Sewing and Fabric Crafts');
insert into category(name) values('Woodworking');
insert into category(name) values('Paper Crafts');
insert into category(name) values('Jewelry Making');
insert into category(name) values('Outdoor/Garden Crafts');
insert into category(name) values('Holiday and Seasonal Crafts');
insert into category(name) values('Kids Crafts');

insert into complexity(name) values('Beginner');
insert into complexity(name) values('Intermediate');
insert into complexity(name) values('Advanced');
insert into complexity(name) values('Expert');

INSERT INTO users(username, email, password) VALUES ('john_doe', 'john@example.com', 'password123');
INSERT INTO users(username, email, password) VALUES ('jane_smith', 'jane@example.com', 'securePassword');
INSERT INTO users(username, email, password) VALUES ('alice_wonderland', 'alice@example.com', 's3cr3t!');
INSERT INTO users(username, email, password) VALUES ('bob_marley', 'bob@example.com', 'reggae123');
INSERT INTO users(username, email, password) VALUES ('emma_jones', 'emma@example.com', 'emmaPassword');

INSERT INTO project (title, user_id, category_id, complexity_id, description, content)
VALUES ('DIY Mason Jar Lanterns', 1, 1, 1, 'Create beautiful lanterns using mason jars',
        'To make DIY Mason Jar Lanterns, start by cleaning the mason jars thoroughly and letting them dry ' ||
        'completely. Choose acrylic paint colors of your preference and paint the exterior of the jars. ' ||
        'Once the paint has dried, apply a layer of sealant to protect the paint. Decorate the jars with ' ||
        'adhesive gems, ribbons, or other decorative elements as desired. Insert LED candles or fairy lights');

INSERT INTO project (title, user_id, category_id, complexity_id, description, content)
VALUES ('DIY Fabric Wall Art', 2, 3, 2, 'Create unique fabric wall art to add personality to your space',
        'To make DIY Fabric Wall Art, start by selecting a fabric that complements your decor theme. Stretch ' ||
        'the fabric over a wooden frame or canvas and secure it tightly using a staple gun. Trim any excess ' ||
        'fabric for a neat finish. Next, embellish the fabric with embroidered designs, fabric paints, or ' ||
        'appliques to add texture and visual interest. Once your design is complete, hang the fabric wall ' ||
        'art using picture hooks or adhesive strips. Enjoy your personalized wall decor!');

