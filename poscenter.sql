
DROP TABLE IF EXISTS `adjust_price_bill`;
CREATE TABLE `adjust_price_bill` (
  `shop_id` int(11) NOT NULL COMMENT '店面id',
  `id` int(11) NOT NULL COMMENT '调价单id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `bill_number` varchar(20) DEFAULT NULL COMMENT '调价单号',
  `target_price` double DEFAULT NULL COMMENT '目标价',
  `audit_status` int(1) DEFAULT NULL COMMENT '审核状态[1:待审核；2:审核通过；3:审核未通过]',
  `use_status` int(1) DEFAULT NULL COMMENT '使用状态[1:未提交；2:已提交]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`shop_id`,`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调价单';

DROP TABLE IF EXISTS `auth_company`;
CREATE TABLE `auth_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '公司名称',
  `telephone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='公司';

DROP TABLE IF EXISTS `auth_department`;
CREATE TABLE `auth_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` int(11) DEFAULT NULL COMMENT '公司id',
  `name` varchar(50) NOT NULL COMMENT '部门名称',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='部门';

DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu` (
  `id` varchar(50) NOT NULL DEFAULT '' COMMENT '记录号',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父菜单',
  `res_id` varchar(50) DEFAULT NULL COMMENT '关联资源',
  `menu_name` varchar(20) NOT NULL COMMENT '菜单名称',
  `menu_desc` varchar(100) DEFAULT NULL COMMENT '菜单描述',
  `tree_level` int(11) DEFAULT NULL COMMENT '树级别',
  `is_leaf` int(1) DEFAULT NULL COMMENT '是否叶子节点[0:不是；1:是]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单';

INSERT INTO `auth_menu` VALUES ('M01', null, 'R01', '系统管理', '', '1', '0', '2013-06-14 15:02:35');
INSERT INTO `auth_menu` VALUES ('M0101', 'M01', 'R0101', '用户管理', '', '2', '1', '2013-06-14 15:02:49');
INSERT INTO `auth_menu` VALUES ('M0102', 'M01', 'R0102', '资源管理', '', '2', '1', '2013-06-14 15:02:59');
INSERT INTO `auth_menu` VALUES ('M0103', 'M01', 'R0103', '菜单管理', '', '2', '1', '2013-06-14 15:03:16');
INSERT INTO `auth_menu` VALUES ('M0104', 'M01', 'R0104', '部门管理', '', '2', '1', '2013-06-14 15:26:55');
INSERT INTO `auth_menu` VALUES ('M0105', 'M01', 'R0105', '机构管理', '', '2', '1', '2013-06-14 15:27:35');
INSERT INTO `auth_menu` VALUES ('M0106', 'M01', 'R0106', '角色管理', '', '2', '1', '2013-06-17 15:18:08');
INSERT INTO `auth_menu` VALUES ('M02', null, 'R02', '会员管理', '', '1', '0', '2013-06-17 13:31:07');
INSERT INTO `auth_menu` VALUES ('M0201', 'M02', 'R0201', '会员管理', '', '2', '1', '2013-06-17 13:31:26');
INSERT INTO `auth_menu` VALUES ('M03', null, 'R03', '店面管理', '', '1', '0', '2013-06-17 13:32:19');
INSERT INTO `auth_menu` VALUES ('M0301', 'M03', 'R0301', '店面信息管理', '', '2', '1', '2013-06-17 13:32:33');
INSERT INTO `auth_menu` VALUES ('M0302', 'M03', 'R0302', '店面库存管理', '', '2', '1', '2013-06-17 13:32:50');
INSERT INTO `auth_menu` VALUES ('M0303', 'M03', 'R0303', '店面进销存', '', '2', '1', '2013-06-17 13:33:04');
INSERT INTO `auth_menu` VALUES ('M0304', 'M03', 'R0304', '店面销售订单', '', '2', '1', '2013-06-17 13:33:18');
INSERT INTO `auth_menu` VALUES ('M0305', 'M03', 'R0305', '店面租赁订单', '', '2', '1', '2013-06-17 13:33:31');
INSERT INTO `auth_menu` VALUES ('M0306', 'M03', 'R0306', '店面系统管理', '', '2', '1', '2013-06-17 13:33:48');
INSERT INTO `auth_menu` VALUES ('M0307', 'M03', 'R0307', '店面红蓝卡管理', '店面红蓝卡管理', '2', '1', '2013-06-25 15:36:54');
INSERT INTO `auth_menu` VALUES ('M0308', 'M03', 'R0308', '充值单管理', '', '2', '1', '2013-06-26 15:43:49');
INSERT INTO `auth_menu` VALUES ('M0309', 'M03', 'R0309', '审核充值单', '', '2', '1', '2013-06-26 17:11:27');
INSERT INTO `auth_menu` VALUES ('M0310', 'M03', 'R0310', '审核调价单', '', '2', '1', '2013-06-27 14:30:13');
INSERT INTO `auth_menu` VALUES ('M04', null, 'R04', '商品管理', '', '1', '0', '2013-06-17 13:34:10');
INSERT INTO `auth_menu` VALUES ('M0401', 'M04', 'R0401', '商品信息管理', '', '2', '1', '2013-06-17 13:34:29');
INSERT INTO `auth_menu` VALUES ('M0402', 'M04', 'R0402', '商品分类管理', '', '2', '1', '2013-06-17 13:34:44');
INSERT INTO `auth_menu` VALUES ('M05', null, 'R05', '供应商', '', '1', '0', '2013-06-17 13:35:00');
INSERT INTO `auth_menu` VALUES ('M0501', 'M05', 'R0501', '供应商管理', '', '2', '1', '2013-06-17 13:35:16');
INSERT INTO `auth_menu` VALUES ('M06', null, 'R06', '出入库', '', '1', '0', '2013-06-17 13:35:31');
INSERT INTO `auth_menu` VALUES ('M0601', 'M06', 'R0601', '采购单管理', '', '2', '1', '2013-06-17 13:35:44');
INSERT INTO `auth_menu` VALUES ('M0602', 'M06', 'R0602', '收货单管理', '', '2', '1', '2013-06-17 13:36:04');
INSERT INTO `auth_menu` VALUES ('M0603', 'M06', 'R0603', '发货单管理', '', '2', '1', '2013-06-17 13:36:15');
INSERT INTO `auth_menu` VALUES ('M0604', 'M06', 'R0604', '店面退货单', '', '2', '1', '2013-06-17 13:36:26');
INSERT INTO `auth_menu` VALUES ('M07', null, 'R07', '促销活动', '', '1', '0', '2013-06-17 13:36:42');
INSERT INTO `auth_menu` VALUES ('M0701', 'M07', 'R0701', '促销活动管理', '', '2', '1', '2013-06-17 13:37:05');
INSERT INTO `auth_menu` VALUES ('M08', null, 'R08', '进销存', '', '1', '0', '2013-06-17 13:37:18');
INSERT INTO `auth_menu` VALUES ('M0801', 'M08', 'R0801', '进销存记录', '', '2', '1', '2013-06-17 13:37:37');
INSERT INTO `auth_menu` VALUES ('M09', null, 'R09', '盘点', '', '1', '0', '2013-06-17 13:37:48');
INSERT INTO `auth_menu` VALUES ('M0901', 'M09', 'R0901', '盘点列表', '', '2', '1', '2013-06-17 13:54:00');

DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `id` varchar(50) NOT NULL DEFAULT '' COMMENT '主键',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父ID',
  `res_name` varchar(50) NOT NULL COMMENT '资源名称',
  `res_url` varchar(100) NOT NULL COMMENT '资源路径',
  `res_desc` varchar(100) DEFAULT NULL COMMENT '资源描述',
  `tree_level` int(2) DEFAULT NULL COMMENT '树级别',
  `is_leaf` int(1) DEFAULT NULL COMMENT '是否叶子节点[0:不是；1:是]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源';

INSERT INTO `auth_resource` VALUES ('R01', null, '系统管理', '1', '', '1', '0', '2013-06-14 14:58:22');
INSERT INTO `auth_resource` VALUES ('R0101', 'R01', '用户管理', '/pos/user!userList.do', '', '2', '1', '2013-06-14 14:59:13');
INSERT INTO `auth_resource` VALUES ('R0102', 'R01', '资源管理', '/poscenter/auth/resource/resourceList.jsp', '', '2', '1', '2013-06-14 15:00:10');
INSERT INTO `auth_resource` VALUES ('R0103', 'R01', '菜单管理', '/poscenter/auth/menu/menuList.jsp', '', '2', '1', '2013-06-14 15:00:34');
INSERT INTO `auth_resource` VALUES ('R0104', 'R01', '部门管理', '/pos/department!departmentList.do', '', '2', '1', '2013-06-14 15:22:34');
INSERT INTO `auth_resource` VALUES ('R0105', 'R01', '机构管理', '/pos/company!companyList.do', '', '2', '1', '2013-06-14 15:22:55');
INSERT INTO `auth_resource` VALUES ('R0106', 'R01', '角色管理', '/pos/role!roleList.do', '', '2', '1', '2013-06-17 15:17:32');
INSERT INTO `auth_resource` VALUES ('R02', null, '会员管理', '1', '', '1', '0', '2013-06-17 09:05:12');
INSERT INTO `auth_resource` VALUES ('R0201', 'R02', '会员管理', '/pos/member!memberList.do', '', '2', '1', '2013-06-17 09:05:42');
INSERT INTO `auth_resource` VALUES ('R03', null, '店面管理', '1', '', '1', '0', '2013-06-17 09:06:55');
INSERT INTO `auth_resource` VALUES ('R0301', 'R03', '店面信息管理', '/pos/shop!shopList.do', '', '2', '1', '2013-06-17 09:07:42');
INSERT INTO `auth_resource` VALUES ('R0302', 'R03', '店面库存管理', '/pos/shopStock!productList.do', '', '2', '1', '2013-06-17 09:08:39');
INSERT INTO `auth_resource` VALUES ('R0303', 'R03', '店面进销存', '/pos/shopInvoice!productList.do', '', '2', '1', '2013-06-17 09:09:12');
INSERT INTO `auth_resource` VALUES ('R0304', 'R03', '店面销售订单', '/pos/shopOrder!shopSaledOrderList.do', '', '2', '1', '2013-06-17 09:09:43');
INSERT INTO `auth_resource` VALUES ('R0305', 'R03', '店面租赁订单', '/pos/shopOrder!shopLeaseOrderList.do', '', '2', '1', '2013-06-17 09:10:13');
INSERT INTO `auth_resource` VALUES ('R0306', 'R03', '店面系统管理', '/pos/shopVersion!shopVersionList.do', '', '2', '1', '2013-06-17 09:10:51');
INSERT INTO `auth_resource` VALUES ('R0307', 'R03', '店面红蓝卡管理', '/pos/priceCard!priceCardList.do', '店面红蓝卡信息管理', '2', '1', '2013-06-25 15:34:32');
INSERT INTO `auth_resource` VALUES ('R0308', 'R03', '充值单管理', '/pos/rechargeOrder!rechargeOrderList.do', '', '2', '1', '2013-06-26 15:41:52');
INSERT INTO `auth_resource` VALUES ('R0309', 'R03', '审核充值单', '/pos/rechargeOrder!auditRechargeOrderList.do', '', '2', '1', '2013-06-26 17:10:56');
INSERT INTO `auth_resource` VALUES ('R0310', 'R03', '审核调价单', '/pos/adjustPriceBill!adjustPriceBillList.do', '', '2', '1', '2013-06-27 14:29:37');
INSERT INTO `auth_resource` VALUES ('R04', null, '商品管理', '1', '', '1', '0', '2013-06-17 09:11:32');
INSERT INTO `auth_resource` VALUES ('R0401', 'R04', '商品信息管理', '/pos/Product!productList.do', '', '2', '1', '2013-06-17 09:12:00');
INSERT INTO `auth_resource` VALUES ('R0402', 'R04', '商品分类管理', '/poscenter/goodsclass/goodsClassList.jsp', '', '2', '1', '2013-06-17 09:12:28');
INSERT INTO `auth_resource` VALUES ('R05', null, '供应商管理', '1', '', '1', '0', '2013-06-17 09:12:59');
INSERT INTO `auth_resource` VALUES ('R0501', 'R05', '供应商管理', '/pos/supplier!supplierList.do', '', '2', '1', '2013-06-17 09:13:24');
INSERT INTO `auth_resource` VALUES ('R06', null, '出入库管理', '1', '', '1', '0', '2013-06-17 09:14:02');
INSERT INTO `auth_resource` VALUES ('R0601', 'R06', '采购单管理', '/pos/purchaseOrder!purchaseOrderList.do', '', '2', '1', '2013-06-17 09:14:37');
INSERT INTO `auth_resource` VALUES ('R0602', 'R06', '收货单管理', '/pos/receiveOrder!getReceiveOrderList.do', '', '2', '1', '2013-06-17 09:15:08');
INSERT INTO `auth_resource` VALUES ('R0603', 'R06', '发货单管理', '/pos/sendOrder!sendOrderList.do', '', '2', '1', '2013-06-17 09:15:35');
INSERT INTO `auth_resource` VALUES ('R0604', 'R06', '店面退货单管理', '/pos/shopReturnOrder!shopReturnOrderList.do', '', '2', '1', '2013-06-17 09:16:30');
INSERT INTO `auth_resource` VALUES ('R07', null, '促销活动', '1', '', '1', '0', '2013-06-17 09:17:01');
INSERT INTO `auth_resource` VALUES ('R0701', 'R07', '促销活动管理', '/pos/event!eventList.do', '', '2', '1', '2013-06-17 09:17:28');
INSERT INTO `auth_resource` VALUES ('R08', null, '进销存', '1', '', '1', '0', '2013-06-17 09:18:04');
INSERT INTO `auth_resource` VALUES ('R0801', 'R08', '进销存记录', '/pos/invoice!productList.do', '', '2', '1', '2013-06-17 09:18:27');
INSERT INTO `auth_resource` VALUES ('R09', null, '盘点', '1', '', '1', '0', '2013-06-17 09:19:01');
INSERT INTO `auth_resource` VALUES ('R0901', 'R09', '盘点列表', '/pos/checkStock!checkStockList.do', '', '2', '1', '2013-06-17 09:19:24');
INSERT INTO `auth_resource` VALUES ('R10', null, '非菜单资源', '1', '', '1', '0', '2013-06-17 09:21:06');
INSERT INTO `auth_resource` VALUES ('R1001', 'R10', '会员管理', '2', '', '2', '0', '2013-06-17 09:28:18');
INSERT INTO `auth_resource` VALUES ('R100101', 'R1001', '导入会员', 'importMember', '', '3', '1', '2013-06-17 09:28:38');
INSERT INTO `auth_resource` VALUES ('R1002', 'R10', '店面管理', '2', '', '2', '0', '2013-06-17 09:29:25');
INSERT INTO `auth_resource` VALUES ('R100201', 'R1002', '新增店面', 'addShop', '', '3', '1', '2013-06-17 09:29:47');
INSERT INTO `auth_resource` VALUES ('R100202', 'R1002', '修改店面', 'updateShop', '', '3', '1', '2013-06-17 09:30:01');
INSERT INTO `auth_resource` VALUES ('R100203', 'R1002', '删除店面', 'deleteShop', '', '3', '1', '2013-06-17 09:32:25');
INSERT INTO `auth_resource` VALUES ('R100204', 'R1002', '新增店面系统版本', 'addShopVersion', '', '3', '1', '2013-06-17 09:44:35');
INSERT INTO `auth_resource` VALUES ('R100205', 'R1002', '修改店面系统版本', 'updateShopVersion', '', '3', '1', '2013-06-17 09:44:48');
INSERT INTO `auth_resource` VALUES ('R100206', 'R1002', '删除店面系统版本', 'deleteShopVersion', '', '3', '1', '2013-06-17 09:45:00');
INSERT INTO `auth_resource` VALUES ('R100207', 'R1002', '发布店面系统版本', 'releaseShopVersion', '', '3', '1', '2013-06-17 09:46:10');
INSERT INTO `auth_resource` VALUES ('R1003', 'R10', '供应商管理', '2', '', '2', '0', '2013-06-17 09:32:51');
INSERT INTO `auth_resource` VALUES ('R100301', 'R1003', '新增供应商', 'addSupplier', '', '3', '1', '2013-06-17 09:33:10');
INSERT INTO `auth_resource` VALUES ('R100302', 'R1003', '修改供应商', 'updateSupplier', '', '3', '1', '2013-06-17 09:33:37');
INSERT INTO `auth_resource` VALUES ('R100303', 'R1003', '删除供应商', 'deleteSupplier', '', '3', '1', '2013-06-17 09:33:53');
INSERT INTO `auth_resource` VALUES ('R1004', 'R10', '商品管理', '2', '', '2', '0', '2013-06-17 09:34:24');
INSERT INTO `auth_resource` VALUES ('R100401', 'R1004', '新增商品分类', 'addGoodsClass', '', '3', '1', '2013-06-17 09:34:41');
INSERT INTO `auth_resource` VALUES ('R100402', 'R1004', '修改商品分类', 'updateGoodsClass', '', '3', '1', '2013-06-17 09:35:00');
INSERT INTO `auth_resource` VALUES ('R100403', 'R1004', '删除商品分类', 'deleteGoodsClass', '', '3', '1', '2013-06-17 09:35:19');
INSERT INTO `auth_resource` VALUES ('R100404', 'R1004', '新增商品', 'addProduct', '', '3', '1', '2013-06-17 09:35:40');
INSERT INTO `auth_resource` VALUES ('R100405', 'R1004', '修改商品', 'updateProduct', '', '3', '1', '2013-06-17 09:35:54');
INSERT INTO `auth_resource` VALUES ('R100406', 'R1004', '删除商品', 'deleteProduct', '', '3', '1', '2013-06-17 09:36:06');
INSERT INTO `auth_resource` VALUES ('R100407', 'R1004', '导入商品', 'importProduct', '', '3', '1', '2013-06-17 09:36:41');
INSERT INTO `auth_resource` VALUES ('R1006', 'R10', '盘点', '2', '', '2', '0', '2013-06-17 09:47:19');
INSERT INTO `auth_resource` VALUES ('R100601', 'R1006', '新增盘点任务', 'addCheckStockTask', '', '3', '1', '2013-06-17 09:48:04');
INSERT INTO `auth_resource` VALUES ('R100602', 'R1006', '提交盘点任务', 'submitCheckStockTask', '', '3', '1', '2013-06-19 15:16:02');
INSERT INTO `auth_resource` VALUES ('R100603', 'R1006', '查看电脑库存', 'showComputerStock', '', '3', '1', '2013-06-19 15:23:20');
INSERT INTO `auth_resource` VALUES ('R1007', 'R10', '促销活动', '2', '', '2', '0', '2013-06-17 09:49:13');
INSERT INTO `auth_resource` VALUES ('R100701', 'R1007', '新增促销活动', 'addEvent', '', '3', '1', '2013-06-17 09:49:30');
INSERT INTO `auth_resource` VALUES ('R100702', 'R1007', '修改促销活动', 'updateEvent', '', '3', '1', '2013-06-17 09:49:46');
INSERT INTO `auth_resource` VALUES ('R100703', 'R1007', '删除促销活动', 'deleteEvent', '', '3', '1', '2013-06-17 09:50:07');
INSERT INTO `auth_resource` VALUES ('R1008', 'R10', '采购单', '2', '', '2', '0', '2013-06-19 15:43:41');
INSERT INTO `auth_resource` VALUES ('R100801', 'R1008', '新增采购单', 'addPurchaseOrder', '', '3', '1', '2013-06-19 15:44:13');
INSERT INTO `auth_resource` VALUES ('R100802', 'R1008', '修改采购单', 'updatePurchaseOrder', '', '3', '1', '2013-06-19 15:44:35');
INSERT INTO `auth_resource` VALUES ('R100803', 'R1008', '删除采购单', 'deletePurchaseOrder', '', '3', '1', '2013-06-19 15:44:56');
INSERT INTO `auth_resource` VALUES ('R100804', 'R1008', '导入采购单', 'importPurchaseOrder', '', '3', '1', '2013-06-19 15:45:27');
INSERT INTO `auth_resource` VALUES ('R100805', 'R1008', '新增采购单条目', 'addPurchaseOrderItem', '', '3', '1', '2013-06-19 15:48:42');
INSERT INTO `auth_resource` VALUES ('R100806', 'R1008', '修改采购单条目', 'updatePurchaseOrderItem', '', '3', '1', '2013-06-19 15:49:02');
INSERT INTO `auth_resource` VALUES ('R100807', 'R1008', '删除采购单条目', 'deletePurchaseOrderItem', '', '3', '1', '2013-06-19 15:50:58');
INSERT INTO `auth_resource` VALUES ('R100808', 'R1008', '查看采购单采购价格', 'showPurchaseOrderPrice', '', '3', '1', '2013-06-19 15:51:24');
INSERT INTO `auth_resource` VALUES ('R100809', 'R1008', '提交采购单', 'submitPurchaseOrder', '', '3', '1', '2013-06-19 15:51:49');
INSERT INTO `auth_resource` VALUES ('R1009', 'R10', '收货单', '2', '', '2', '0', '2013-06-19 15:45:54');
INSERT INTO `auth_resource` VALUES ('R100901', 'R1009', '确认收货单', 'confirmReceiveOrder', '', '3', '1', '2013-06-19 15:46:10');
INSERT INTO `auth_resource` VALUES ('R1010', 'R10', '发货单', '2', '', '2', '0', '2013-06-19 15:46:31');
INSERT INTO `auth_resource` VALUES ('R101001', 'R1010', '新增发货单', 'addSendOrder', '', '3', '1', '2013-06-19 15:46:46');
INSERT INTO `auth_resource` VALUES ('R101002', 'R1010', '修改发货单', 'updateSendOrder', '', '3', '1', '2013-06-19 15:47:06');
INSERT INTO `auth_resource` VALUES ('R101003', 'R1010', '删除发货单', 'deleteSendOrder', '', '3', '1', '2013-06-19 15:47:20');
INSERT INTO `auth_resource` VALUES ('R101004', 'R1010', '提交发货单', 'submitSendOrder', '', '3', '1', '2013-06-19 15:53:51');
INSERT INTO `auth_resource` VALUES ('R101005', 'R1010', '新增发货单条目', 'addSendOrderItem', '', '3', '1', '2013-06-19 15:57:07');
INSERT INTO `auth_resource` VALUES ('R101006', 'R1010', '修改发货单条目', 'updateSendOrderItem', '', '3', '1', '2013-06-19 15:58:02');
INSERT INTO `auth_resource` VALUES ('R101007', 'R1010', '删除发货单条目', 'deleteSendOrderItem', '', '3', '1', '2013-06-19 15:58:24');
INSERT INTO `auth_resource` VALUES ('R1011', 'R10', '店面退货单', '2', '', '2', '0', '2013-06-19 15:47:48');
INSERT INTO `auth_resource` VALUES ('R101101', 'R1011', '确认店面退货单', 'confirmShopReturnOrder', '', '3', '1', '2013-06-19 15:48:15');
INSERT INTO `auth_resource` VALUES ('R1012', 'R10', '红蓝卡管理', '2', '店面的红蓝卡管理', '2', '0', '2013-06-26 15:13:56');
INSERT INTO `auth_resource` VALUES ('R101201', 'R1012', '新增红蓝卡', 'addPriceCard', '新增红蓝卡', '3', '1', '2013-06-26 15:14:24');
INSERT INTO `auth_resource` VALUES ('R1013', 'R10', '充值单管理', '2', '', '2', '0', '2013-06-26 15:42:13');
INSERT INTO `auth_resource` VALUES ('R101301', 'R1013', '新增充值单', 'addRechargeOrder', '', '3', '1', '2013-06-26 15:42:45');
INSERT INTO `auth_resource` VALUES ('R101302', 'R1013', '修改充值单', 'updateRechargeOrder', '', '3', '1', '2013-06-26 15:43:02');
INSERT INTO `auth_resource` VALUES ('R101303', 'R1013', '删除充值单', 'deleteRechargeOrder', '', '3', '1', '2013-06-26 15:43:19');

DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录号',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_ch_name` varchar(50) DEFAULT NULL COMMENT '角色中文名称',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色';

INSERT INTO `auth_role` VALUES ('1', 'admin', '超级管理员', '', '2013-06-01 00:00:00');

DROP TABLE IF EXISTS `auth_role_resource`;
CREATE TABLE `auth_role_resource` (
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `res_id` varchar(50) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`,`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源中间表';

INSERT INTO `auth_role_resource` VALUES ('1', 'R01');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0101');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0102');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0103');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0104');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0105');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0106');
INSERT INTO `auth_role_resource` VALUES ('1', 'R02');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0201');
INSERT INTO `auth_role_resource` VALUES ('1', 'R03');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0301');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0302');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0303');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0304');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0305');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0306');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0307');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0308');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0309');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0310');
INSERT INTO `auth_role_resource` VALUES ('1', 'R04');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0401');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0402');
INSERT INTO `auth_role_resource` VALUES ('1', 'R05');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0501');
INSERT INTO `auth_role_resource` VALUES ('1', 'R06');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0601');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0602');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0603');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0604');
INSERT INTO `auth_role_resource` VALUES ('1', 'R07');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0701');
INSERT INTO `auth_role_resource` VALUES ('1', 'R08');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0801');
INSERT INTO `auth_role_resource` VALUES ('1', 'R09');
INSERT INTO `auth_role_resource` VALUES ('1', 'R0901');
INSERT INTO `auth_role_resource` VALUES ('1', 'R10');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1001');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100101');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1002');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100201');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100202');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100203');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100204');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100205');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100206');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100207');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1003');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100301');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100302');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100303');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1004');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100401');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100402');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100403');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100404');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100405');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100406');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100407');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1006');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100601');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100602');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100603');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1007');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100701');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100702');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100703');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1008');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100801');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100802');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100803');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100804');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100805');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100806');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100807');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100808');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100809');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1009');
INSERT INTO `auth_role_resource` VALUES ('1', 'R100901');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1010');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101001');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101002');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101003');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101004');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101005');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101006');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101007');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1011');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101101');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1012');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101201');
INSERT INTO `auth_role_resource` VALUES ('1', 'R1013');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101301');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101302');
INSERT INTO `auth_role_resource` VALUES ('1', 'R101303');

DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录号',
  `department_id` int(11) DEFAULT NULL COMMENT '所属部门',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `user_password` varchar(50) DEFAULT NULL COMMENT '密码',
  `user_real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `use_status` int(1) DEFAULT NULL COMMENT '使用状态[1:有效；2:禁用]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户';

INSERT INTO `auth_user` VALUES ('1', '0', 'admin', '8wAW9RYebN3WPpalOe__Yg))', '超级管理员', '1', '2013-06-10 11:52:21');

DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色中间表';

INSERT INTO `auth_user_role` VALUES ('1', '1');

DROP TABLE IF EXISTS `buy_gift_event`;
CREATE TABLE `buy_gift_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_count` int(11) DEFAULT NULL COMMENT '商品数量',
  `user_limit` int(11) DEFAULT NULL COMMENT '人数限制',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='买赠活动';

DROP TABLE IF EXISTS `buy_gift_product`;
CREATE TABLE `buy_gift_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `buy_gift_event_id` int(11) DEFAULT NULL COMMENT '买赠活动id',
  `gift_product_id` int(11) DEFAULT NULL COMMENT '赠送商品id',
  `max_gift_count` int(11) DEFAULT NULL COMMENT '最大赠送数量',
  PRIMARY KEY (`id`),
  KEY `idx_buy_gift_event_id` (`buy_gift_event_id`) USING BTREE,
  KEY `idx_gift_product_id` (`gift_product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='买赠活动赠品';

DROP TABLE IF EXISTS `check_stock`;
CREATE TABLE `check_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `charger` varchar(50) DEFAULT NULL COMMENT '盘点负责人',
  `check_time` datetime DEFAULT NULL COMMENT '盘点时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `use_status` int(11) DEFAULT NULL COMMENT '使用状态[0:未提交；1:已提交]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='盘点表';

DROP TABLE IF EXISTS `check_stock_item`;
CREATE TABLE `check_stock_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_stock_id` int(11) DEFAULT NULL COMMENT '所属盘点id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `charger` varchar(50) DEFAULT NULL COMMENT '盘点负责人',
  `purchase_price` double DEFAULT NULL COMMENT '进价',
  `sale_price` double DEFAULT NULL COMMENT '售价',
  `count` int(11) DEFAULT NULL COMMENT '盘点数量',
  `stock` int(11) DEFAULT NULL COMMENT '电脑库存',
  `check_time` datetime DEFAULT NULL COMMENT '盘点时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='盘点条目表';


DROP TABLE IF EXISTS `combo_event`;
CREATE TABLE `combo_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `combo_price` double DEFAULT NULL COMMENT '套餐价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='套餐活动';

DROP TABLE IF EXISTS `combo_product`;
CREATE TABLE `combo_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `combo_event_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='套餐商品';

DROP TABLE IF EXISTS `count_discount_event`;
CREATE TABLE `count_discount_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_count` int(11) DEFAULT NULL COMMENT '数量',
  `type` int(11) DEFAULT NULL COMMENT '折扣类型[1:阶梯折扣;2:固定折扣]',
  `discount` double DEFAULT NULL COMMENT '数值',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='数量折扣活动';

DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '活动类型 ：1-买赠 2-换购 3-金额折扣 4-数量折扣 5-套餐',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `rule_desc` varchar(500) DEFAULT NULL COMMENT '规则说明',
  `use_status` int(1) DEFAULT NULL COMMENT '使用状态[1:未生效；2:已生效；3:已过期]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='活动';

DROP TABLE IF EXISTS `goods_class`;
CREATE TABLE `goods_class` (
  `id` varchar(20) NOT NULL DEFAULT '' COMMENT '记录号',
  `parent_id` varchar(20) DEFAULT NULL COMMENT '父分类Id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `tree_level` int(11) DEFAULT NULL COMMENT '级别',
  `is_leaf` int(11) DEFAULT NULL COMMENT '是否叶子节点[0:不是；1:是]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品分类';


DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(10) DEFAULT NULL COMMENT '商品id',
  `serial_number` varchar(20) DEFAULT NULL COMMENT '流水单号[采购单号、发货单号]',
  `oper_type` varchar(1) DEFAULT NULL COMMENT '操作类型[0:收货；1:发货；2:店面退货]',
  `before_count` int(10) DEFAULT NULL COMMENT '操作前商品库存数量',
  `count` int(10) DEFAULT NULL COMMENT '商品变动数量',
  `after_count` int(10) DEFAULT NULL COMMENT '操作后商品库存数量',
  `oper_user` varchar(255) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_serial_number` (`serial_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='进销存记录';


DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` varchar(30) NOT NULL COMMENT '会员id',
  `name` varchar(10) DEFAULT NULL COMMENT '会员姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '会员身份证号',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `register_time` datetime DEFAULT NULL COMMENT '会员注册时间',
  `use_state` int(1) DEFAULT '2' COMMENT '使用状态[0:使用中; 1:已注销;2:新卡;3:挂失]',
  `change_time` datetime DEFAULT NULL COMMENT '更改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员';


DROP TABLE IF EXISTS `money_discount_event`;
CREATE TABLE `money_discount_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `money` double DEFAULT NULL COMMENT '金额',
  `type` varchar(255) DEFAULT NULL COMMENT '类型[1:减钱 2：折扣]',
  `number_value` double DEFAULT NULL COMMENT '数值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='金额折扣活动';


DROP TABLE IF EXISTS `price_card`;
CREATE TABLE `price_card` (
  `id` varchar(30) NOT NULL DEFAULT '' COMMENT '调价卡卡号',
  `shop_id` int(10) DEFAULT NULL COMMENT '店铺id',
  `clerk_name` varchar(20) DEFAULT NULL COMMENT '店员姓名',
  `open_time` datetime DEFAULT NULL COMMENT '开卡时间',
  `type` int(2) DEFAULT NULL COMMENT '类型[1:红卡，2:蓝卡]',
  `state` int(2) DEFAULT NULL COMMENT '调价卡状态[1:白卡【未初始化】   2:可使用【可进行消费、充值】    3:停用【不可消费，授权后可转换成可使用】]',
  `supplier_id` int(10) DEFAULT NULL COMMENT '供应商id',
  `password` varchar(100) DEFAULT NULL COMMENT '卡密码',
  `point` double unsigned DEFAULT '0' COMMENT '卡内点数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调价卡（红蓝卡）';


DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `goods_class_id` varchar(20) DEFAULT NULL COMMENT '商品分类id',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供货商id',
  `bar_code` char(255) DEFAULT NULL COMMENT '条形码',
  `name` char(255) DEFAULT NULL COMMENT '商品名称',
  `lease_price` double DEFAULT NULL COMMENT '日租赁价格',
  `month_lease_price` double DEFAULT NULL COMMENT '包月租赁价格',
  `deposit` double DEFAULT NULL COMMENT '押金',
  `sale_price` double DEFAULT NULL COMMENT '销售价(标牌价)',
  `limit_price` double DEFAULT NULL COMMENT '限价',
  `lock_price` double DEFAULT NULL COMMENT '锁价',
  `red_lines` double DEFAULT NULL COMMENT '红卡额度',
  `blue_lines` double DEFAULT NULL COMMENT '蓝卡额度',
  `stock` int(11) DEFAULT NULL COMMENT '库存量',
  `purchase_refer_price` double DEFAULT NULL COMMENT '采购参考价',
  `is_delete` int(1) DEFAULT '0' COMMENT '是否删除[0:未删除；1:已删除]',
  PRIMARY KEY (`id`),
  KEY `idx_bar_code` (`bar_code`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE,
  KEY `idx_class_id` (`goods_class_id`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品表';


DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '采购单id',
  `order_number` varchar(20) DEFAULT NULL COMMENT '采购单号',
  `charger` varchar(50) DEFAULT NULL COMMENT '负责人',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `use_status` int(11) DEFAULT NULL COMMENT '使用状态[0:未提交；1:已提交；2:已归档]',
  `department` varchar(20) DEFAULT NULL COMMENT '采购单位',
  PRIMARY KEY (`id`),
  KEY `idx_order_number` (`order_number`,`use_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='采购单';

DROP TABLE IF EXISTS `purchase_order_item`;
CREATE TABLE `purchase_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '采购单条目id',
  `purchase_order_id` int(11) DEFAULT NULL COMMENT '所属采购单id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商id',
  `count` int(11) DEFAULT NULL COMMENT '商品数量',
  `purchase_price` double DEFAULT NULL COMMENT '采购价',
  PRIMARY KEY (`id`),
  KEY `idx_purchase_order_id` (`purchase_order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='采购单条目';


DROP TABLE IF EXISTS `receive_order`;
CREATE TABLE `receive_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(10) DEFAULT NULL COMMENT '收货单号',
  `charger` varchar(255) DEFAULT NULL COMMENT '收货负责人',
  `create_time` datetime DEFAULT NULL COMMENT '收货时间',
  `purchase_id` int(11) DEFAULT NULL COMMENT '采购单号',
  `is_acquired` int(1) unsigned zerofill DEFAULT '0' COMMENT '是否确认收货0--未确认  1--已确认',
  PRIMARY KEY (`id`),
  KEY `idx_number_requier` (`order_number`,`is_acquired`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='收货单';


DROP TABLE IF EXISTS `receive_order_item`;
CREATE TABLE `receive_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(10) DEFAULT NULL COMMENT '收货单号',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商id',
  `receive_count` int(11) DEFAULT NULL COMMENT '实收商品数量',
  `send_count` int(11) DEFAULT NULL COMMENT '发货数量',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='收货单条目';


DROP TABLE IF EXISTS `recharge_order`;
CREATE TABLE `recharge_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `price_card_id` varchar(50) DEFAULT NULL COMMENT '调价卡id',
  `point` double DEFAULT NULL COMMENT '充值点数',
  `audit_status` int(1) DEFAULT NULL COMMENT '审核状态[1:待审核；2:审核通过；3:审核未通过]',
  `use_status` int(1) DEFAULT NULL COMMENT '使用状态[1:未提交；2:已提交]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_price_card_id` (`price_card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='充值单';


DROP TABLE IF EXISTS `send_order`;
CREATE TABLE `send_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发货单id',
  `order_number` varchar(20) DEFAULT NULL COMMENT '发货单号',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `charger` varchar(50) DEFAULT NULL COMMENT '负责人',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `use_status` int(11) DEFAULT NULL COMMENT '使用状态[0:未提交；1:已提交；2:已归档]',
  `department` varchar(255) DEFAULT NULL COMMENT '收货单位',
  PRIMARY KEY (`id`),
  KEY `idx_number_status` (`order_number`,`use_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='发货单';

DROP TABLE IF EXISTS `send_order_item`;
CREATE TABLE `send_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发货单条目id',
  `send_order_id` int(11) DEFAULT NULL COMMENT '所属发货单id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `count` int(11) DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (`id`),
  KEY `idx_send_id` (`send_order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='发货单条目';


DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) DEFAULT NULL COMMENT '店面编号',
  `name` varchar(100) DEFAULT NULL COMMENT '店面名称',
  `address` varchar(200) DEFAULT NULL COMMENT '店面地址',
  `ip_address` varchar(20) DEFAULT NULL COMMENT '店面的ip地址',
  `charger` varchar(30) DEFAULT NULL COMMENT '负责人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面';

DROP TABLE IF EXISTS `shop_event`;
CREATE TABLE `shop_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面活动';


DROP TABLE IF EXISTS `shop_invoice`;
CREATE TABLE `shop_invoice` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `product_id` int(10) DEFAULT NULL COMMENT '商品id',
  `serial_number` varchar(20) DEFAULT NULL COMMENT '流水单号[采购单号、发货单号]',
  `oper_type` varchar(1) DEFAULT NULL COMMENT '操作类型[0:收货；1:销售；2:退货；3:租赁；4:还租；5:盘点；6:退货到中心库]',
  `before_count` int(10) DEFAULT NULL COMMENT '操作前商品库存数量',
  `count` int(10) DEFAULT NULL COMMENT '商品变动数量',
  `after_count` int(10) DEFAULT NULL COMMENT '操作后商品库存数量',
  `oper_user` varchar(255) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_serial_number` (`serial_number`) USING BTREE,
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面进销存记录';


DROP TABLE IF EXISTS `shop_lease_order`;
CREATE TABLE `shop_lease_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `lease_order_id` int(10) DEFAULT NULL,
  `serial_number` char(20) DEFAULT NULL COMMENT '订单流水号',
  `pos_code` varchar(20) DEFAULT NULL COMMENT 'pos机编号',
  `cashier_id` int(10) DEFAULT NULL COMMENT '收银员id',
  `member_id` varchar(30) DEFAULT NULL COMMENT '会员id',
  `price` double DEFAULT NULL COMMENT '订单总金额',
  `deposit` double DEFAULT NULL COMMENT '订单总押金/应归还的总押金',
  `order_type` int(11) DEFAULT NULL COMMENT '订单类型[0:租赁订单；1:还租订单]',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `shop_id` varchar(255) DEFAULT NULL COMMENT '店面编码',
  PRIMARY KEY (`id`),
  KEY `idx_lease_id` (`lease_order_id`,`order_type`) USING BTREE,
  KEY `idx_serial_number` (`serial_number`) USING BTREE,
  KEY `idx_member_id` (`member_id`) USING BTREE,
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='租赁订单';


DROP TABLE IF EXISTS `shop_lease_order_product`;
CREATE TABLE `shop_lease_order_product` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `lease_order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `pre_price` double DEFAULT NULL COMMENT '单价',
  `per_deposit` double DEFAULT NULL COMMENT '单押金',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '赁租开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '租赁结束时间',
  `time_length` double DEFAULT NULL COMMENT '租赁时长',
  `lease_style` int(1) DEFAULT NULL COMMENT '租赁方式[0:按日租赁；1:包月租赁]',
  `shop_id` varchar(255) DEFAULT NULL COMMENT '店面编码',
  PRIMARY KEY (`id`),
  KEY `idx_lease_order_id` (`lease_order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='租赁订单商品';


DROP TABLE IF EXISTS `shop_money_destination`;
CREATE TABLE `shop_money_destination` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `order_id` int(11) DEFAULT NULL COMMENT '退货订单id',
  `type` int(11) DEFAULT NULL COMMENT '去向类型[1:现金；2:会员卡；3:银行卡]',
  `money` double DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单的金钱去向';


DROP TABLE IF EXISTS `shop_money_source`;
CREATE TABLE `shop_money_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '金钱来源id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `order_id` int(11) DEFAULT NULL COMMENT '销售订单id',
  `type` int(11) DEFAULT NULL COMMENT '来源类型[1:现金；2:会员卡；3:银行卡]',
  `money` double DEFAULT NULL COMMENT '金额',
  `swip_card_number` varchar(255) DEFAULT NULL COMMENT '刷卡单号',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单的金钱来源';


DROP TABLE IF EXISTS `shop_return_order`;
CREATE TABLE `shop_return_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '退货单id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `order_number` varchar(20) DEFAULT NULL COMMENT '退货单号',
  `charger` varchar(50) DEFAULT NULL COMMENT '负责人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `use_status` int(11) DEFAULT NULL COMMENT '使用状态[0:未确认；1:已确认]',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`) USING BTREE,
  KEY `idx_order_number` (`order_number`,`use_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面退货单';


DROP TABLE IF EXISTS `shop_return_order_item`;
CREATE TABLE `shop_return_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '店面退货单条目id',
  `shop_return_order_id` int(11) DEFAULT NULL COMMENT '所属店面退货单id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `return_count` int(11) DEFAULT NULL COMMENT '退货数量',
  `receive_count` int(11) DEFAULT NULL COMMENT '实收数量',
  PRIMARY KEY (`id`),
  KEY `idx_shop_return_order_id` (`shop_return_order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面退货单条目';


DROP TABLE IF EXISTS `shop_saled_order`;
CREATE TABLE `shop_saled_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `saled_order_id` int(11) DEFAULT NULL,
  `serial_number` char(20) DEFAULT NULL COMMENT '订单流水号',
  `pos_code` varchar(20) DEFAULT NULL COMMENT 'pos机编号',
  `cashier_id` int(10) DEFAULT NULL COMMENT '收银员id',
  `member_id` varchar(30) DEFAULT NULL COMMENT '会员id',
  `price` double DEFAULT NULL COMMENT '单订总金额',
  `saled_time` timestamp NULL DEFAULT NULL COMMENT '销售时间',
  `order_type` int(1) DEFAULT NULL COMMENT '订单类型[0-销售订单,1-退货订单]',
  `shop_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_saled_order_id` (`saled_order_id`) USING BTREE,
  KEY `idx_serial_number` (`serial_number`) USING BTREE,
  KEY `idx_member_id` (`member_id`) USING BTREE,
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='销售订单';


DROP TABLE IF EXISTS `shop_saled_order_event`;
CREATE TABLE `shop_saled_order_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_event_id` int(11) DEFAULT NULL COMMENT '店面活动表的主键id',
  `saled_order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `detail_event_id` int(11) DEFAULT NULL COMMENT '具体的活动id',
  `ext_id` int(11) DEFAULT NULL COMMENT '额外的比如赠品等记录的id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  PRIMARY KEY (`id`),
  KEY `idx_shop_event_id` (`shop_event_id`) USING BTREE,
  KEY `idx_saled_order_id` (`saled_order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面订单活动中间表';

DROP TABLE IF EXISTS `shop_saled_order_product`;
CREATE TABLE `shop_saled_order_product` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `saled_order_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '购买数量',
  `pre_price` double DEFAULT NULL COMMENT '价单',
  `order_type` int(1) DEFAULT NULL COMMENT '订单类型[0-销售订单,1-退货订单]',
  `event_remark` varchar(255) DEFAULT NULL COMMENT '活动备注信息',
  `shop_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_saled_order_id` (`saled_order_id`) USING BTREE,
  KEY `idx_shop_id` (`shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='销售订单商品';


DROP TABLE IF EXISTS `shop_stock`;
CREATE TABLE `shop_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `create_time` datetime DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面库存';


DROP TABLE IF EXISTS `shop_version`;
CREATE TABLE `shop_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `version_number` varchar(50) DEFAULT NULL COMMENT '版本号',
  `version_name` varchar(100) DEFAULT NULL COMMENT '版本名称',
  `version_desc` varchar(500) DEFAULT NULL COMMENT '版本描述',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `create_time` datetime DEFAULT NULL COMMENT '发布时间',
  `use_status` int(11) DEFAULT NULL COMMENT '使用状态[1:未发布；2:已发布]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店面系统版本';

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `is_delete` int(10) unsigned DEFAULT '0' COMMENT '删除标记 0-未删除  1--已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='供应商';

DROP TABLE IF EXISTS `swap_buy_event`;
CREATE TABLE `swap_buy_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL COMMENT '活动id',
  `money` double DEFAULT NULL COMMENT '金额',
  `append_money` double DEFAULT NULL COMMENT '添加金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='换购活动';

DROP TABLE IF EXISTS `swap_buy_product`;
CREATE TABLE `swap_buy_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `swap_buy_event_id` int(11) DEFAULT NULL COMMENT '换购活动id',
  `gift_product_id` int(11) DEFAULT NULL COMMENT '赠送商品id',
  PRIMARY KEY (`id`),
  KEY `idx_swap_buy_event_id` (`swap_buy_event_id`) USING BTREE,
  KEY `idx_gift_product_id` (`gift_product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='换购活动商品';

DROP TABLE IF EXISTS `temp_recharge_update`;
CREATE TABLE `temp_recharge_update` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price_card_id` varchar(50) DEFAULT NULL COMMENT '红蓝卡id',
  `point` double DEFAULT NULL COMMENT '充值点数',
  `shop_id` int(11) DEFAULT NULL COMMENT '店面id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='红蓝卡临时充值数据';


