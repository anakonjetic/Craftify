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

/*password = newPassword123*/
INSERT INTO users(name, username, email, password, is_admin, is_private) VALUES ('John Doe','john_doe', 'john@example.com', '$2a$10$AI4p3eAvDflvwMAcKwvFfeT0rsv9CC5U8ewX1lk/KLuvoDe5GU5Ae', false, false);
INSERT INTO users(name, username, email, password, is_admin, is_private) VALUES ('Jane Smith','jane_smith', 'jane@example.com', '$2a$10$AI4p3eAvDflvwMAcKwvFfeT0rsv9CC5U8ewX1lk/KLuvoDe5GU5Ae', false, false);
INSERT INTO users(name, username, email, password, is_admin, is_private) VALUES ('Alice','alice_wonderland', 'alice@example.com', '$2a$10$AI4p3eAvDflvwMAcKwvFfeT0rsv9CC5U8ewX1lk/KLuvoDe5GU5Ae', true, true);
INSERT INTO users(name, username, email, password, is_admin, is_private) VALUES ('Bob Marley','bob_marley', 'bob@example.com', '$2a$10$AI4p3eAvDflvwMAcKwvFfeT0rsv9CC5U8ewX1lk/KLuvoDe5GU5Ae', false, false);
INSERT INTO users(name, username, email, password, is_admin, is_private) VALUES ('Emma Jones','emma_jones', 'emma@example.com', '$2a$10$AI4p3eAvDflvwMAcKwvFfeT0rsv9CC5U8ewX1lk/KLuvoDe5GU5Ae', false, true);

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

insert into favorites(user_id, project_id) VALUES(2,1);

insert into user_preferences(user_id, category_id) VALUES(1,2);
insert into user_preferences(user_id, category_id) VALUES(1,3);
insert into user_preferences(user_id, category_id) VALUES(1,4);
insert into user_preferences(user_id, category_id) VALUES(2,1);

insert into project_subscribers(user_id, project_id) VALUES(1,2);
insert into user_subscribers(user_id, followed_user_id) VALUES(1,2);
insert into user_project_likes(user_id, project_id) VALUES(1,2);

insert into media(media, MEDIA_ORDER, project_id) VALUES('url',1,1);

insert into comment (user_id, project_id, comment, comment_time)
VALUES (1,1,'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed varius diam, sit amet ' ||
            'cursus urna. Morbi tempor elit a lectus ultricies bibendum. Maecenas in dictum massa, ac ' ||
            'rhoncus lorem. Morbi varius leo et lacus fermentum lobortis.', CURRENT_TIME);

insert into comment (user_id, project_id, comment, comment_time)
VALUES (2,1,'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed varius diam, sit amet ' ||
            'cursus urna. Morbi tempor elit a lectus ultricies bibendum. Maecenas in dictum massa, ac ' ||
            'rhoncus lorem. Morbi varius leo et lacus fermentum lobortis.', CURRENT_TIME);

insert into comment (user_id, project_id, comment, comment_time)
VALUES (2,2,'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed varius diam, sit amet ' ||
    'cursus urna. Morbi tempor elit a lectus ultricies bibendum. Maecenas in dictum massa, ac ' ||
    'rhoncus lorem. Morbi varius leo et lacus fermentum lobortis.', '2024-05-13 12:00:00');

insert into comment (user_id, project_id, comment, comment_time, parent_comment_id)
VALUES (1,2,'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed varius diam, sit amet ' ||
            'cursus urna. Morbi tempor elit a lectus ultricies bibendum. Maecenas in dictum massa, ac ' ||
            'rhoncus lorem. Morbi varius leo et lacus fermentum lobortis.', CURRENT_TIME, 3);

INSERT INTO news (title, content, category_id, image_url)
VALUES ('New DIY Project: Transform Your Living Space with Upcycled Decor',
        'Turn your home into a cozy haven with these DIY decor ideas. Learn how to upcycle everyday items ' ||
        'into stylish home accents.',2,'url');

INSERT INTO news (title, content, category_id, image_url)
VALUES ('Creative Sewing Projects for Beginners and Beyond',
        'Discover fun sewing projects for all skill levels. From simple stitches to intricate designs, ' ||
        'there''s something for everyone.', 3,'url');

INSERT INTO news (title, content, category_id, image_url)
VALUES ('Woodworking Wonders: Build Your Own Furniture with These Easy Plans',
        'Unleash your creativity with woodworking. Learn how to build beautiful furniture pieces for ' ||
        'your home with step-by-step guides.', 4,'url');

INSERT INTO news (title, content, category_id, image_url)
VALUES ('Crafting Fun for the Whole Family: Paper Crafts Galore!',
        'Get crafty with paper! Explore a world of creativity with these paper craft ideas. From ' ||
        'origami to cardmaking, let your imagination soar.', 5,'url');


INSERT INTO tutorial (title, content, user_id, category_id, complexity_id) VALUES
        ('DIY Paper Flowers', 'Learn how to make beautiful paper flowers with this easy step-by-step tutorial...', 1, 1, 1),
        ('Build a Wooden Birdhouse', 'This project will guide you through building a charming wooden birdhouse...', 2, 2, 2);

INSERT INTO media (media, media_order, project_id, tutorial_id) VALUES
        ('https://www.12x12cardstock.shop/cdn/shop/articles/paper_flowers_by_rosy_46939f0d-f9bf-4d17-873b-5dfb34bc087b_1400x.jpg?v=1650583476', 1, NULL, 1),
        ('https://www.wainfleetradingpost.com/cdn/shop/files/barnbirdhouse-h_1500x1500.png?v=1698630725', 1, NULL, 2)
