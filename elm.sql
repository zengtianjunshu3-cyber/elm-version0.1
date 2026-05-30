/*
 Navicat Premium Dump SQL

 Source Server         : localmysql
 Source Server Type    : MySQL
 Source Server Version : 90700 (9.7.0)
 Source Host           : localhost:3306
 Source Schema         : elm

 Target Server Type    : MySQL
 Target Server Version : 90700 (9.7.0)
 File Encoding         : 65001

 Date: 30/05/2026 17:23:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business
-- ----------------------------
DROP TABLE IF EXISTS `business`;
CREATE TABLE `business`  (
  `businessId` int NOT NULL AUTO_INCREMENT COMMENT '商家编号',
  `businessName` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家名称',
  `businessAddress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商家地址',
  `businessExplain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商家介绍',
  `businessImg` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家图片',
  `orderTypeId` int NOT NULL COMMENT '点餐分类',
  `starPrice` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '起送费',
  `deliveryPrice` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '配送费',
  `remarks` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`businessId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business
-- ----------------------------
INSERT INTO `business` VALUES (1, '肯德基', '上海市黄浦区', '肯德基宅急送', 'kfc_img', 1, 15.00, 5.00, NULL);
INSERT INTO `business` VALUES (6, 'BBM', '上海市浦东新区南汇新城镇沪城环路 999 号 84 幢 2006-2008', '欢迎光临', '872b3a8a-feaa-4f21-9584-a9e7e0578c90.png', 6, 0.00, 0.00, '无');
INSERT INTO `business` VALUES (7, '正宗金牌猪脚饭', '共享区店', '欢迎光临本店！我们坚持现炒浇头、拒绝预制菜，食材新鲜现炒，为您的健康保驾护航。', '7791691d-812c-42e6-b9fa-4acc408a1ba9.png', 7, 0.00, 3.00, '无');
INSERT INTO `business` VALUES (8, '辣风芹', '临港店', '新鲜现炒，拒绝预制', 'ac2fdd15-a2ca-4798-a65f-2c6e36931db4.png', 8, 0.00, 5.00, '无');
INSERT INTO `business` VALUES (9, 'Lucky Coffee', '共享区', '无', 'd9e011a8-f811-47df-be91-eb344ed46821.png', 5, 0.00, 0.00, '无');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cartId` int NOT NULL AUTO_INCREMENT COMMENT '无意义编号',
  `foodId` int NOT NULL COMMENT '食品编号',
  `businessId` int NOT NULL COMMENT '所属商家编号',
  `userId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户编号',
  `quantity` int NOT NULL COMMENT '同一类型食品的购买数量',
  PRIMARY KEY (`cartId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for deliveryaddress
-- ----------------------------
DROP TABLE IF EXISTS `deliveryaddress`;
CREATE TABLE `deliveryaddress`  (
  `daId` int NOT NULL AUTO_INCREMENT COMMENT '送货地址编号',
  `contactName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名',
  `contactSex` int NOT NULL COMMENT '联系人性别',
  `contactTel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人电话',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '送货地址',
  `userId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户编号',
  PRIMARY KEY (`daId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of deliveryaddress
-- ----------------------------
INSERT INTO `deliveryaddress` VALUES (1, 'xiaohei', 1, '1008611', '上海海洋大学四号门', '18139871346');

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`  (
  `foodId` int NOT NULL AUTO_INCREMENT COMMENT '食品编号',
  `foodName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '食品名称',
  `foodExplain` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '食品介绍',
  `foodImg` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '食品图片',
  `foodPrice` decimal(5, 2) NOT NULL COMMENT '食品价格',
  `businessId` int NOT NULL COMMENT '所属商家编号',
  `remarks` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`foodId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of food
-- ----------------------------
INSERT INTO `food` VALUES (1, '香辣鸡腿堡', '经典美味', 'burger_img', 18.00, 1, NULL);
INSERT INTO `food` VALUES (2, '猪脚饭', '健康美味', 'c534b2de-dbb0-4552-99c0-af7e8a996875.png', 13.00, 7, '无');
INSERT INTO `food` VALUES (3, '小炒肉盖浇面', '偏辣', '013a21d0-7180-4242-afae-f55a43abb18e.png', 19.00, 6, '无');
INSERT INTO `food` VALUES (4, '鸡肉炒米粉', '新鲜现炒', '59edfc8c-e648-4905-af4a-8530318c70b9.png', 16.00, 8, '无');
INSERT INTO `food` VALUES (5, '茉莉花茶拿铁', '无', '84b63298-14a1-49b4-a605-fee64cb2a14a.png', 21.00, 9, '无');

-- ----------------------------
-- Table structure for orderdetailet
-- ----------------------------
DROP TABLE IF EXISTS `orderdetailet`;
CREATE TABLE `orderdetailet`  (
  `odId` int NOT NULL AUTO_INCREMENT COMMENT '订单明细编号',
  `orderId` int NOT NULL COMMENT '所属订单编号',
  `foodId` int NOT NULL COMMENT '所属食品编号',
  `quantity` int NOT NULL COMMENT '数量',
  PRIMARY KEY (`odId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderdetailet
-- ----------------------------
INSERT INTO `orderdetailet` VALUES (7, 7, 1, 1);
INSERT INTO `orderdetailet` VALUES (8, 8, 1, 1);
INSERT INTO `orderdetailet` VALUES (9, 9, 1, 1);
INSERT INTO `orderdetailet` VALUES (10, 10, 1, 2);
INSERT INTO `orderdetailet` VALUES (11, 11, 1, 1);
INSERT INTO `orderdetailet` VALUES (12, 12, 1, 1);
INSERT INTO `orderdetailet` VALUES (13, 13, 1, 1);
INSERT INTO `orderdetailet` VALUES (14, 14, 1, 1);
INSERT INTO `orderdetailet` VALUES (15, 15, 1, 1);
INSERT INTO `orderdetailet` VALUES (16, 16, 1, 1);
INSERT INTO `orderdetailet` VALUES (17, 17, 1, 1);
INSERT INTO `orderdetailet` VALUES (18, 18, 1, 1);
INSERT INTO `orderdetailet` VALUES (19, 19, 1, 1);
INSERT INTO `orderdetailet` VALUES (20, 20, 1, 1);
INSERT INTO `orderdetailet` VALUES (21, 21, 4, 1);
INSERT INTO `orderdetailet` VALUES (22, 22, 4, 1);
INSERT INTO `orderdetailet` VALUES (23, 23, 3, 1);
INSERT INTO `orderdetailet` VALUES (24, 25, 3, 1);
INSERT INTO `orderdetailet` VALUES (25, 26, 5, 1);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `orderId` int NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `userId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户编号',
  `businessId` int NOT NULL COMMENT '所属商家编号',
  `orderDate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订购日期',
  `orderTotal` decimal(7, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `daId` int NOT NULL COMMENT '所属送货地址编号',
  `orderState` int NOT NULL DEFAULT 0 COMMENT '订单状态（0：未支付； 1：已支付）',
  `usedRedEnvelope` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '支付时使用的红包金额',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (7, '18139871346', 1, '2026-05-30 14:54:25', 21.50, 1, 1, 0.00);
INSERT INTO `orders` VALUES (8, '18139871346', 1, '2026-05-30 15:00:53', 20.70, 1, 1, 0.00);
INSERT INTO `orders` VALUES (9, '18139871346', 1, '2026-05-30 15:01:25', 20.70, 1, 1, 0.00);
INSERT INTO `orders` VALUES (10, '18139871346', 1, '2026-05-30 15:08:28', 39.57, 1, 1, 0.00);
INSERT INTO `orders` VALUES (11, '18139871346', 1, '2026-05-30 15:11:38', 19.65, 1, 1, 1.31);
INSERT INTO `orders` VALUES (12, '18139871346', 1, '2026-05-30 15:11:50', 19.39, 1, 1, 1.31);
INSERT INTO `orders` VALUES (13, '18139871346', 1, '2026-05-30 15:13:22', 19.39, 1, 1, 1.31);
INSERT INTO `orders` VALUES (14, '18139871346', 1, '2026-05-30 15:15:09', 18.08, 1, 1, 0.00);
INSERT INTO `orders` VALUES (15, '18139871346', 1, '2026-05-30 15:17:18', 19.29, 1, 1, 1.41);
INSERT INTO `orders` VALUES (16, '18139871346', 1, '2026-05-30 15:17:56', 19.45, 1, 1, 1.25);
INSERT INTO `orders` VALUES (17, '18139871346', 1, '2026-05-30 15:23:10', 20.66, 1, 1, 1.23);
INSERT INTO `orders` VALUES (18, '18139871346', 1, '2026-05-30 15:43:31', 20.70, 1, 1, 0.00);
INSERT INTO `orders` VALUES (19, '18139871346', 1, '2026-05-30 16:35:58', 18.35, 1, 1, 2.41);
INSERT INTO `orders` VALUES (20, '18139871346', 1, '2026-05-30 16:36:35', 19.39, 1, 1, 1.31);
INSERT INTO `orders` VALUES (21, '18139871346', 8, '2026-05-30 16:54:25', 17.32, 1, 1, 1.58);
INSERT INTO `orders` VALUES (22, '18139871346', 8, '2026-05-30 16:57:36', 18.90, 1, 1, 0.00);
INSERT INTO `orders` VALUES (23, '18139871346', 6, '2026-05-30 17:03:51', 15.07, 1, 1, 2.03);
INSERT INTO `orders` VALUES (25, '18139871346', 6, '2026-05-30 17:20:16', 15.11, 1, 1, 1.99);
INSERT INTO `orders` VALUES (26, '18139871346', 9, '2026-05-30 17:22:55', 19.86, 1, 1, 0.00);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `userName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `userSex` int NOT NULL DEFAULT 1 COMMENT '用户性别（1：男； 0：女）',
  `userImg` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户头像',
  `delTag` int NOT NULL DEFAULT 1 COMMENT '删除标记（1：正常； 0：删除）',
  `userType` int NOT NULL DEFAULT 0,
  `isVip` int NOT NULL DEFAULT 0 COMMENT '是否为VIP（0：否； 1：是）',
  `redEnvelope` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '红包余额',
  `goldCoins` int NULL DEFAULT 0 COMMENT '金币余额',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('18139871346', '123456', 'xiaohei', 0, '2e656905-405d-4d28-b512-d311fb0a0a30.png', 1, 0, 1, 3.21, 4281);
INSERT INTO `user` VALUES ('admin', 'admin123', '系统管理员奶鼠', 1, '823c9afa-7b81-4c1c-a1c1-75763d2b2a5e.png', 1, 1, 1, 0.00, 0);
INSERT INTO `user` VALUES ('admin111', '123456', '管理员奶叔', 1, 'eb4be337-55f4-4fd4-8eee-c0621966f51a.png', 1, 1, 1, 0.00, 0);

SET FOREIGN_KEY_CHECKS = 1;
