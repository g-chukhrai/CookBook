/*
MySQL Backup
Source Server Version: 5.1.41
Source Database: cookbook
Date: 09.03.2011 17:21:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
--  Table structure for `amount`
-- ----------------------------
DROP TABLE IF EXISTS `amount`;
CREATE TABLE `amount` (
  `amount_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`amount_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `category`
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `category_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `feedback`
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `feed_id` int(20) NOT NULL AUTO_INCREMENT,
  `user_account_id` int(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `response` varchar(1000) NOT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`feed_id`),
  KEY `user_acc_id` (`user_account_id`),
  CONSTRAINT `user_acc_id` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ingridient`
-- ----------------------------
DROP TABLE IF EXISTS `ingridient`;
CREATE TABLE `ingridient` (
  `ingridient_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` int(30) DEFAULT NULL,
  PRIMARY KEY (`ingridient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `mark`
-- ----------------------------
DROP TABLE IF EXISTS `mark`;
CREATE TABLE `mark` (
  `mark_id` int(20) NOT NULL AUTO_INCREMENT,
  `value` double(2,0) NOT NULL,
  `description` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`mark_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `recipe`
-- ----------------------------
DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe` (
  `recipe_id` int(20) NOT NULL AUTO_INCREMENT,
  `category_id` int(20) NOT NULL,
  `user_account_id` int(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `process` varchar(1000) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image_name` varchar(1000) DEFAULT NULL,
  `cook_time` int(20) DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`recipe_id`),
  KEY `category_fk` (`category_id`),
  KEY `user_account_id_fk` (`user_account_id`),
  CONSTRAINT `recipe_category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_account_id_fk` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `recipe_mark`
-- ----------------------------
DROP TABLE IF EXISTS `recipe_mark`;
CREATE TABLE `recipe_mark` (
  `recipe_mark_id` int(20) NOT NULL AUTO_INCREMENT,
  `recipe_id` int(20) NOT NULL,
  `mark_id` int(20) NOT NULL,
  `user_account_id` int(20) NOT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`recipe_mark_id`),
  KEY `recipe_mark_recipe_id_fk` (`recipe_id`) USING BTREE,
  KEY `recipe_mark_mark_id_fk` (`mark_id`) USING BTREE,
  KEY `recipe_mark_user_id_fk` (`user_account_id`) USING BTREE,
  CONSTRAINT `recipe_mark_ibfk_1` FOREIGN KEY (`mark_id`) REFERENCES `mark` (`mark_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `recipe_mark_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `recipe_mark_ibfk_3` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `recipe_product`
-- ----------------------------
DROP TABLE IF EXISTS `recipe_product`;
CREATE TABLE `recipe_product` (
  `product_id` int(20) NOT NULL AUTO_INCREMENT,
  `recipe_id` int(20) NOT NULL,
  `ingridient_id` int(20) NOT NULL,
  `amount_id` int(20) NOT NULL,
  `amount_size` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `ingridient_id_fk` (`ingridient_id`) USING BTREE,
  KEY `amount_id_fk` (`amount_id`) USING BTREE,
  KEY `recipe_id_fk` (`recipe_id`),
  CONSTRAINT `amount_id_fk` FOREIGN KEY (`amount_id`) REFERENCES `amount` (`amount_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ingridient_id_fk` FOREIGN KEY (`ingridient_id`) REFERENCES `ingridient` (`ingridient_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `recipe_id_fk` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `user_account`
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `user_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varchar(300) NOT NULL,
  `email` varchar(50) NOT NULL,
  `user_authority_id` int(20) NOT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_account_user_authority_id` (`user_authority_id`),
  CONSTRAINT `user_account_user_authority_id` FOREIGN KEY (`user_authority_id`) REFERENCES `user_authority` (`user_authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_authority`
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority` (
  `user_authority_id` int(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`user_authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO `amount` VALUES ('1','литр'), ('2','килограмм'), ('3','грамм'), ('4','милиграмм'), ('5','чайная ложка'), ('6','столовая ложка'), ('7','стакан'), ('8','по вкусу'), ('9','штука'), ('10','r'), ('11','23'), ('12','йцу'), ('13','ккк'), ('14','wdas'), ('15','ацуфацф'), ('16','ee'), ('17','d'), ('18','we'), ('19','пакета'), ('20','q'), ('21','e');
INSERT INTO `category` VALUES ('1','Супы','Супы – блюда поистине универсальные. Они не только очень полезны для здоровья, но при этом еще очень выручают хозяйку, ведь кастрюлей ароматного супа можно накормить большую семью. В этом разделе мы собрали для вас лучшие рецепты и на каждый день, и на праздник.'), ('2','Основные блюда','Вторые блюда должны присутствовать в обеде, чтобы обед считался полноценным и законченным. Вариантов их приготовления огромное множество, рецептов – еще больше, ведь одно блюдо с разными соусами может иметь совершенно разный вкус. Чтобы узнать интересные и оригинальные способы приготовления мяса, рыбы, овощей и многих других продуктов, загляните в данный раздел'), ('3','Закуски и салаты','Салаты и закуски обязательно входят в меню любого праздника. Их рецептов сегодня – великое множество. Овощные, мясные, рыбные, фруктовые, из сыра, колбасы и даже из хлеба – перечислять можно очень долго. Гораздо проще заглянуть в рецепты, представленные в соответствующих разделах, и выбрать те из них, что придутся вам по вкусу.раздничный стол, но и, без сомнения, станут любимыми блюдами в вашей семье.'), ('4','Бутерброды и сэндвичи','Закуски являются неотъемлемой частью угощения для всех, кто стремится соблюдать все правила и требования этикета. В этом разделе мы собрали лучшие рецепты закусок, которые помогут не только накрыть праздничный стол, но и, без сомнения, станут любимыми блюдами в вашей семье.'), ('5','Выпечка и десерты','Выпечка – сколько прелести и домашнего уюта таится в этом слове! Всем, кто хочет баловать своих домашних нежнейшими пирогами, булочками, ватрушками из дрожжевого, слоеного, бисквитного, песочного теста, мы предлагаем рецепты выпечки оригинальных и очень вкусных изделий.'), ('6','Соусы','Соусы – это настоящий шедевр французской кулинарии, который сегодня занял почетное место и на столах россиян. Самое простое и незамысловатое блюдо эта удивительная подлива сможет сделать уникальным и неповторимым. Заглянув в этот раздел, вы значительно пополните копилку своих рецептов легкими в приготовлении, но по-настоящему волшебными блюдами.'), ('7','Напитки и коктейли','Напитки, способные разбудить аппетит, утолить жажду, согреть в холода – настоящая находка для хозяек. А коктейльные вечеринки, получившие сегодня очень широкое распространение, просто не могут без них обойтись. Тонкости, секреты, рецепты, советы – все это в данном разделе.'), ('8','Варенье и джемы','Варенья, джемы, сиропы обязательно варит каждая уважающая себя хозяйка. Чай с вкусным и полезным витаминным вареньем – даже сама эта фраза звучит очень уютно и по-домашнему. Разнообразные рецепты, советы, способы приготовления этих лакомств мы рады предложить вам в данном разделе. ');
INSERT INTO `feedback` VALUES ('1',NULL,'chu@chu.chu','chuee','2011-02-27 19:34:40'), ('2','1',NULL,'хороша','2011-02-27 19:35:46');
INSERT INTO `ingridient` VALUES ('1','картофель',NULL), ('2','морковь',NULL), ('3','лук',NULL), ('4','чеснок',NULL), ('5','курятина',NULL), ('6','телятина',NULL), ('7','яйцо',NULL), ('8','мука',NULL), ('9','соль',NULL), ('10','перец',NULL), ('11','капуста',NULL), ('12','огурец',NULL), ('13','вареная колбаса',NULL), ('14','йцу',NULL), ('15','кровь',NULL), ('16','топор',NULL), ('17','федор',NULL), ('18','w',NULL), ('19','fish',NULL), ('20','уц',NULL), ('21','ванильный сахар',NULL), ('22','грецкие орехи',NULL), ('23','шоколад',NULL), ('24','сахар',NULL), ('25','яичный белок',NULL), ('26','разрыхлитель',NULL), ('27','горький шоколад',NULL), ('28','сливочное масло',NULL), ('29','v',NULL);
INSERT INTO `mark` VALUES ('1','1','shit'), ('2','2','too bad'), ('3','3','not bad'), ('4','4','nice'), ('5','5','the best');
INSERT INTO `recipe` VALUES ('1','2','1','мясо по-французски','готовить','фигня','51.jpg;52.jpg',NULL,'2011-01-28 17:01:38'), ('8','8','1','qwi','qeoi','wqei','noimage.png;',NULL,'2011-01-04 17:25:00'), ('26','1','1','asdad','вфвфыв','фывфыв','noimage.png;',NULL,'2011-02-10 00:12:59'), ('28','4','1','test','test1','test2','noimage.png;',NULL,'2011-02-24 22:32:05'), ('29','4','1','ttt','sss','ddd','noimage.png;',NULL,'2011-02-24 22:37:25'), ('31','1','1','фывфыв','фыв','фыв','noimage.png;',NULL,'2011-02-24 23:05:13'), ('32','1','1','фывфыв','фыв','фыв','noimage.png;',NULL,'2011-02-24 23:06:01'), ('33','2','1','фыв','фйцуйцуйуцйуйй\nыва\n\nыва\n\nцу\nацуа','Здесь можно написать дополнительную информацию о рецепте','noimage.png;',NULL,'2011-02-24 23:15:37'), ('34','3','1','алоэ','каноэ','Здесь можно написать дополнительную информацию о рецепте','noimage.png;',NULL,'2011-02-24 23:17:03'), ('38','3','1','сосисочки','сосисочки','готовить','8781113.jpg;9601705.jpg;',NULL,'2011-02-24 23:25:20'), ('39','1','1','козел','жаренный','смас','news_14052_1_MD.jpg;',NULL,'2011-02-24 23:30:45'), ('40','5','1','макароны','варить\nсвернуть\nесть','вася','noimage.png;',NULL,'2011-02-24 23:32:58'), ('41','4','1','ывавыава','цуйцвйув','Здесь можно написать дополнительную информацию о рецепте','yellow.jpg;green.jpg;',NULL,'2011-02-24 23:39:27'), ('42','4','1','фывфы','йцуйу','ккк','00_-_4ekist_-_all_i_know_mixtape_2007_whatrec_cover_192.jpg;',NULL,'2011-02-24 23:45:06'), ('43','3','1','цйу','кккк',NULL,'cover.jpg;',NULL,'2011-02-24 23:46:00'), ('44','3','1','111','1123','23232','noimage.png;',NULL,'2011-02-25 10:26:55'), ('45','3','1','цувфй','цйуйцу','йцуйцуйуй','noimage.png;',NULL,'2011-02-25 10:31:57'), ('46','2','1','фывафв','йцуй','цуйцу','cover.jpg;',NULL,'2011-02-25 10:34:03'), ('47','2','1','козел','йцуй132','цйвыфацфуафывмячсыфцавсч','00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-BACK.jpg;00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-COVER.jpg;00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-CD.jpg;',NULL,'2011-02-25 10:35:18'), ('48','7','1','asd','sdqwq','eqwe','00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-COVER.jpg;00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-SM-COVER.jpg;',NULL,'2011-02-25 10:42:01'), ('49','6','1','Введите название рецепта','asd','sd','00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-CD.jpg;00-Kanye.West-My.Beautiful.Dark.Twisted.Fantasy-(Retail)-2010-[NoFS]-COVER.jpg;',NULL,'2011-02-25 10:45:12'), ('50','2','1','asdad','qweqweqw','qweqweq','noimage.png;',NULL,'2011-02-25 11:17:59'), ('51','7','1','sdffsdf','wqeqwer','qwafdsf','cover.JPG;00-Guf_-_Gorod_Dorog-2007-CD-6aC.JPG;00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;',NULL,'2011-02-25 11:19:01'), ('52','5','1','фывафыва','цфувафыа','уйкйцкукйуцк','noimage.png;',NULL,'2011-02-25 14:19:28'), ('53','1','1','asdad','qweqwe','qweqwe','noimage.png;',NULL,'2011-02-25 14:23:17'), ('54','1','1','Введите название рецепта','Опишите процесс приготовления рецепта',NULL,'noimage.png;',NULL,'2011-02-25 14:25:17'), ('55','3','1','addad','asdfasasdfadsf','asdfasdfasf','noimage.png;',NULL,'2011-02-25 14:26:48'), ('56','3','1','asdad','asdasda','dasdasdasd','dimzavesa_etazhi.jpg;',NULL,'2011-02-25 14:28:26'), ('57','3','1','asdad','qwqwe','weqwe','00_-_underwhat_-_bomjis\'_mixtape_tracklist_cover.jpg;',NULL,'2011-02-25 14:35:06'), ('58','1','1','qwe','qwe','qwe','noimage.png;',NULL,'2011-02-25 14:51:19'), ('59','1','1','new','qwe','qwe','noimage.png;',NULL,'2011-02-25 20:48:55'), ('69','3','1','adsfdasf','dfasdf','sdfasdf','noimage.png;',NULL,'2011-02-25 21:17:37'), ('72','1','1','asd','asdsad','sd','Mezza Morta - Звезда Трущоб (2010).jpg;',NULL,'2011-02-25 21:36:46'), ('73','4','1','sad','asdasd','sdadd','Folder.jpg;',NULL,'2011-02-26 08:40:29'), ('74','2','1','asd','asdd','asdsd','noimage.png;',NULL,'2011-02-26 09:02:25'), ('75','3','1','asd','asdds','asdasd','(3)Folder.jpg;',NULL,'2011-02-26 09:03:03'), ('76','3','1','dd','dd',NULL,'(7)Folder.jpg;',NULL,'2011-02-26 09:05:39'), ('77','2','1','aaa','dddd','ddd','(8)Folder.jpg;',NULL,'2011-02-26 09:08:48'), ('78','2','1','sdd','qwe','we','(9)Folder.jpg;',NULL,'2011-02-26 09:11:42'), ('79','4','1','фыв','йцу','йцу','(10)Folder.jpg;',NULL,'2011-02-26 09:20:13'), ('80','5','1','федор','жарить','василий','(2)00-Guf_-_Gorod_Dorog-2007-CD-6aC.JPG;(2)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;(2)cover.JPG;','140','2011-02-26 12:06:20'), ('81','2','1','Введите название рецепта','Опишите процесс приготовления рецепта',NULL,'noimage.png;','0','2011-02-26 12:08:27'), ('82','2','1','mezza','wqw','eeeee','(3)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;(3)00-Guf_-_Gorod_Dorog-2007-CD-6aC.JPG;(3)cover.JPG;(2)Mezza Morta - Звезда Трущоб (2010).jpg;','30','2011-02-26 15:58:00'), ('83','2','1','privet','qwe',NULL,'(4)Cover.jpg;Cover2.jpg;','20','2011-02-26 16:46:30'), ('84','4','2','asds','qweqe',NULL,'noimage.png;','10','2011-02-26 16:48:01'), ('85','4','1','asd','asd',NULL,'noimage.png;',NULL,'2011-02-26 17:51:13'), ('86','4','1','sd','swde',NULL,'noimage.png;','234','2011-02-26 17:52:50'), ('87','2','15','Жаюролдюзы','Бибири цукик а  цу армжн',NULL,'icon_cheese.png;','40','2011-02-26 18:44:22'), ('88','5','15','Торт \"Графские развалины\"','Приготовить безе.\nНа блюдо выложить слой готовых безешек.\nСверху укладывать безешки, намазывая нижнюю часть каждой безешки масляным кремом на сгущенном молоке.\nТаким образом уложить весь торт в виде горки.\nСверху торт полить растопленным шоколадом и посыпать рублеными грецкими орехами.','Очень вкусный торт!','(2)img0415_3.jpg;(2)img0415_2.jpg;(2)img0415_1.jpg;',NULL,'2011-02-27 09:32:02'), ('89','5','15','Торт \"Трюфель\"','Приготовить тесто, как для пирожных \"Брауниз\".\nКруглую разъемную форму, диаметром не менее 26 см, слегка смазать маслом.\nЗатем застелить пергаментной бумагой, и смазать маслом пергамент.\nГотовое тесто переложить в форму и разровнять поверхность.\nПриготовить тесто, как для пирожных \"Брауниз\".\nКруглую разъемную форму, диаметром не менее 26 см, слегка смазать маслом.\nЗатем застелить пергаментной бумагой, и смазать маслом пергамент.\nГотовое тесто переложить в форму и разровнять поверхность.\nПриготовить крем.\nШоколад порубить ножом.\nСливки влить в кастрюлю и нагреть, не доводя до кипения.\nВ горячие сливки всыпать шоколад.\nПеремешать до однородности (можно оставить на 5 минут, затем снова перемешать, чтобы шоколад хорошо разошелся в сливках).\nТорт убрать в холодильник и дать глазури схватиться.\nПокрыть торт оставшейся глазурью и украсить трюфелями, и шоколадной стружкой.\nУбрать торт в холодильник на несколько часов.\n','Идея этого торта, думаю, многим знакома! Также, многим знаком рецепт пирожных брауниз. Мне захотелось объединить эти два рецепта в один.\nПоскольку, торт шоколадный и довольно сытный, корж нужно выпекать невысоким (это обязательно, иначе торт получится очень сытным).','(2)img0940_7.jpg;(2)img0940_10.jpg;(2)img0940_1.jpg;','300','2011-02-27 09:41:59'), ('90','2','1','sss','sdss',NULL,'(2)(2)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;',NULL,'2011-02-28 22:29:42'), ('91','2','1','dfdf','Опишите процес<span id=\"images\"></span><span id=\"images\"></span>с приго<span id=\"images\"></span>то<span id=\"images\"></span>влени<span id=\"images\"><img style=\"width: 210px; height: 138px;\" id=\"j_idt41:1:j_idt43\" src=\"resources/recipes/%282%29%282%29img0415_3.jpg\" alt=\"\"></span>я рецепта<span id=\"images\"><img style=\"width: 151px; height: 133px;\" id=\"j_idt41:0:j_idt43\" src=\"resources/recipes/%282%29%282%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\"></span><span id=\"images\"></span>',NULL,'(2)(2)(2)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;(2)(2)img0415_3.jpg;','20','2011-02-28 22:56:50'), ('92','4','1','dsf','<span id=\"images\"><img id=\"j_idt41:0:j_idt43\" src=\"resources/recipes/%283%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span><span id=\"images\"><img id=\"j_idt41:0:j_idt43\" src=\"resources/recipes/%283%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span><span id=\"images\"><img id=\"j_idt41:0:j_idt43\" src=\"resources/recipes/%283%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span>',NULL,'(3)(2)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;',NULL,'2011-02-28 23:08:19'), ('93','3','1','sdf','Опишите процесс <span id=\"images\"><img src=\"resources/recipes/%283%29%282%29img0415_3.jpg\" alt=\"\" width=\"100\"></span>приготовл<span id=\"images\"><img src=\"/static/images/%284%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span>ения <span id=\"images\"><img src=\"/static/images/%284%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span>р<span id=\"images\"><img src=\"/static/images/%284%29%282%2900-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG\" alt=\"\" width=\"100\"></span>ецепта',NULL,'(2)(2)52.jpg;',NULL,'2011-02-28 23:16:33'), ('94','2','1','sad','Опишите процasdadasdесс приготовления рецепта',NULL,'(2)(2)(2)(2)(2)00-Guf_-_Gorod_Dorog-2007-Back-6aC.JPG;(2)(2)noimage.png;',NULL,'2011-02-28 23:18:20'), ('95','2','1','ad','<span id=\"images\"><img src=\"/static/images/%282%29%282%29%282%29cover.JPG\" alt=\"\" width=\"100\"></span><span id=\"images\"><img src=\"/static/images/%282%29%282%29%282%29Folder.jpg\" alt=\"\" width=\"100\">hello</span>',NULL,'(2)(2)(2)cover.JPG;(2)(2)(2)Folder.jpg;',NULL,'2011-03-01 09:39:43'), ('96','2','2','asd','<div align=\"center\"><span id=\"images\"><img src=\"/static/images/%282%29%283%2951.jpg\" alt=\"\" width=\"216\" height=\"161\"><br>Размешать, съесть<br></span><span id=\"images\"><img src=\"/static/images/%282%29%283%29img0940_7.jpg\" alt=\"\" width=\"261\" height=\"174\"></span><br></div>','алоэ','(2)(3)51.jpg;(2)(3)52.jpg;(2)(3)img0940_10.jpg;(2)(3)img0940_7.jpg;','50','2011-03-01 12:16:07'), ('97','4','12','пес','приготовить через 5 мин<span id=\"images\"><img style=\"width: 353px; height: 265px;\" src=\"/static/images/ty.jpg\" alt=\"\"></span>',NULL,'ty.jpg;','10','2011-03-01 12:24:55'), ('98','3','1','asd','asdf',NULL,'noimage.png;',NULL,'2011-03-01 13:02:36'), ('99','3','1','asdasd','<h1 class=\"rcptitle fn\" align=\"center\">Блинный пирог с яблоками</h1>\n<p align=\"center\"><img src=\"http://gotovim-doma.ru/img/img1009_1.jpg\" alt=\"Блинный пирог с яблоками\" title=\"Блинный пирог с яблоками\"></p>',NULL,'noimage.png;','20','2011-03-01 13:02:52'), ('100','3','2','фывфы','<div align=\"center\"><span id=\"images\"><img style=\"width: 328px; height: 216px;\" src=\"/static/images/%284%29img0415_1.jpg\" alt=\"\"><br><font color=\"#ff0000\">ывавыывцукуцкуцкуфываф</font></span><font color=\"#ff0000\"><span id=\"images\"></span><span id=\"images\">ы</span></font><span id=\"images\"><br></span><div align=\"left\"><span id=\"images\"><img src=\"/static/images/%284%2952.jpg\" alt=\"\" width=\"224\" height=\"167\"></span><span id=\"images\"></span></div></div>',NULL,'(4)img0415_1.jpg;(5)51.jpg;(4)52.jpg;','20','2011-03-04 13:58:06');
INSERT INTO `recipe_mark` VALUES ('1','1','3','1','2011-02-12 20:55:23'), ('2','33','5','15','2011-02-26 18:47:39'), ('3','80','4','15','2011-02-26 19:05:39'), ('7','34','3','1','2011-02-26 22:32:12'), ('30','83','2','2','2011-02-26 23:07:10'), ('32','87','3','1','2011-02-27 08:50:21'), ('34','87','5','2','2011-02-27 08:51:26'), ('36','87','3','15','2011-02-27 08:52:48'), ('37','81','3','15','2011-02-27 09:16:15'), ('38','52','3','15','2011-02-27 09:16:36'), ('39','82','5','15','2011-02-27 09:25:18'), ('40','89','4','15','2011-02-27 09:57:31'), ('41','92','2','1','2011-02-28 23:11:33'), ('43','97','4','12','2011-03-01 12:28:29'), ('70','100','3','1','2011-03-07 22:36:30'), ('71','75','1','1','2011-03-07 22:36:06');
INSERT INTO `recipe_product` VALUES ('12','26','1','2','1'), ('13','26','2','9','2'), ('14','41','13','9','1'), ('15','52','2','9','1'), ('16','78','2','3','2'), ('17','79','8','3','11'), ('18','79','14','12','1'), ('19','80','15','19','2'), ('20','80','16','9','1'), ('21','80','17','9','1'), ('22','82','18','20','1'), ('23','87','19','6','21'), ('24','87','20','8','22'), ('25','88','21','5','1-2'), ('26','88','22','8',''), ('27','88','23','3','100'), ('28','88','24','7','1'), ('29','88','25','9','4'), ('30','89','8','3','125'), ('31','89','26','5','1'), ('32','89','24','3','250'), ('33','89','27','3','170'), ('34','89','28','3','170'), ('35','89','7','9','3'), ('36','96','7','9','2'), ('37','96','29','21','12'), ('38','96','2','2','1'), ('39','97','1','2','5'), ('40','97','13','3','300'), ('41','97','2','9','3'), ('42','100','1','5','2'), ('43','100','2','2','1');
INSERT INTO `user_account` VALUES ('1','chuger','123','chuger@tut.by','2','2011-02-02 20:25:59'), ('2','zor','321','elennor@yandex.ru','1','2011-02-16 20:26:04'), ('12','sudja','matroskin','sudilar@gmail.com','1','2011-02-18 16:30:52'), ('13','aaa','aaa','aaa@aaa.aaa','1','2011-02-25 16:30:57'), ('14','vasja','123321','cas@sda.Sss','1','2011-02-12 20:28:40'), ('15','qwer','qwer','qwer@qer.qwer','1','2011-02-26 18:46:18'), ('16','asdasd','asdasd','asdasd@asd.asd','1','2011-03-07 22:54:28'), ('17','asda','79fc8846ced02381277b527f9877228af90ffd1a','asdd@adsa.ewr','1','2011-03-08 09:51:51'), ('18','aloe','7e240de74fb1ed08fa08d38063f6a6a91462a815','aaa@aaa.aaa','1','2011-03-08 09:52:27');
INSERT INTO `user_authority` VALUES ('1','ROLE_USER'), ('2','ROLE_ADMIN');
