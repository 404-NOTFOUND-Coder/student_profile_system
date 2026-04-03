-- =============================================
-- 学生用户画像系统 - 完整测试数据
-- 数据量：约100名学生及相关数据
-- =============================================

CREATE DATABASE IF NOT EXISTS student_profile
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE student_profile;

-- =============================================
-- 1. 学生基本信息表 - 100条
-- =============================================
DROP TABLE IF EXISTS sys_student;
CREATE TABLE sys_student (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_no      VARCHAR(20) NOT NULL UNIQUE,
    name            VARCHAR(50) NOT NULL,
    gender          TINYINT DEFAULT 0,
    birthday        DATE,
    phone           VARCHAR(20),
    email           VARCHAR(100),
    nationality     VARCHAR(50) DEFAULT '中国',
    political_status VARCHAR(20),
    enrollment_year INT,
    enrollment_month TINYINT DEFAULT 9,
    college         VARCHAR(100),
    major           VARCHAR(100),
    class_name      VARCHAR(50),
    education_level TINYINT DEFAULT 1,
    student_status  TINYINT DEFAULT 1,
    hometown        VARCHAR(200),
    address         VARCHAR(300),
    avatar_url      VARCHAR(500),
    tags            JSON,
    profile_summary TEXT,
    profile_score   DECIMAL(5,2) DEFAULT 0,
    deleted         TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_no (student_no),
    INDEX idx_college (college),
    INDEX idx_major (major),
    INDEX idx_enrollment_year (enrollment_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学生数据
INSERT INTO sys_student (student_no, name, gender, phone, email, enrollment_year, college, major, class_name, education_level, student_status, political_status, hometown, profile_score) VALUES
('2020010101', '张伟', 1, '1390000101', 'zhangwei@student.edu.cn', 2020, '计算机学院', '计算机科学与技术', '计科1班', 1, 1, '共青团员', '四川成都', 88.5),
('2020010102', '李娜', 2, '1390000102', 'lina@student.edu.cn', 2020, '计算机学院', '软件工程', '软工1班', 1, 1, '中共党员', '北京海淀', 92.3),
('2020010103', '王强', 1, '1390000103', 'wangqiang@student.edu.cn', 2020, '软件学院', '网络工程', '网工1班', 1, 1, '共青团员', '广东深圳', 85.2),
('2020010104', '刘芳', 2, '1390000104', 'liufang@student.edu.cn', 2020, '信息学院', '电子信息工程', '电信1班', 1, 1, '共青团员', '上海浦东', 87.8),
('2020010105', '陈刚', 1, '1390000105', 'chengang@student.edu.cn', 2020, '计算机学院', '信息安全', '信安1班', 1, 4, '中共党员', '浙江杭州', 91.5),
('2021010101', '赵敏', 2, '1390000106', 'zhaomin@student.edu.cn', 2021, '计算机学院', '计算机科学与技术', '计科2班', 1, 1, '共青团员', '江苏南京', 89.2),
('2021010102', '孙浩', 1, '1390000107', 'sunhao@student.edu.cn', 2021, '软件学院', '软件工程', '软工2班', 1, 1, '共青团员', '湖北武汉', 86.5),
('2021010103', '周琳', 2, '1390000108', 'zhoulin@student.edu.cn', 2021, '信息学院', '通信工程', '通信1班', 1, 1, '中共预备党员', '四川成都', 90.1),
('2021010104', '吴昊', 1, '1390000109', 'wuhao@student.edu.cn', 2021, '计算机学院', '物联网工程', '物联1班', 1, 1, '共青团员', '重庆渝北', 84.7),
('2021010105', '郑雪', 2, '1390000110', 'zhengxue@student.edu.cn', 2021, '软件学院', '数据科学', '数科1班', 1, 1, '共青团员', '陕西西安', 88.9),
('2022010101', '王丽', 2, '1390000111', 'wangli@student.edu.cn', 2022, '计算机学院', '计算机科学与技术', '计科3班', 1, 1, '共青团员', '河南郑州', 87.3),
('2022010102', '李明', 1, '1390000112', 'liming@student.edu.cn', 2022, '计算机学院', '软件工程', '软工3班', 1, 1, '共青团员', '山东济南', 85.8),
('2022010103', '陈静', 2, '1390000113', 'chenjing@student.edu.cn', 2022, '信息学院', '电子信息工程', '电信2班', 1, 1, '共青团员', '福建厦门', 89.6),
('2022010104', '张涛', 1, '1390000114', 'zhangtao@student.edu.cn', 2022, '软件学院', '网络工程', '网工2班', 1, 1, '共青团员', '湖南长沙', 83.2),
('2022010105', '刘佳', 2, '1390000115', 'liujia@student.edu.cn', 2022, '计算机学院', '人工智能', '人智1班', 1, 1, '共青团员', '辽宁沈阳', 91.8),
('2022010106', '黄勇', 1, '1390000116', 'huangyong@student.edu.cn', 2022, '软件学院', '软件工程', '软工4班', 1, 1, '共青团员', '江西南昌', 86.1),
('2022010107', '林梅', 2, '1390000117', 'linmei@student.edu.cn', 2022, '信息学院', '自动化', '自动1班', 1, 1, '中共预备党员', '广西南宁', 88.4),
('2022010108', '徐兵', 1, '1390000118', 'xubing@student.edu.cn', 2022, '计算机学院', '信息安全', '信安2班', 1, 1, '共青团员', '山西太原', 84.9),
('2022010109', '何丽', 2, '1390000119', 'helii@student.edu.cn', 2022, '软件学院', '数字媒体', '数媒1班', 1, 1, '共青团员', '云南昆明', 87.7),
('2022010110', '马超', 1, '1390000120', 'machao@student.edu.cn', 2022, '计算机学院', '计算机科学与技术', '计科4班', 1, 1, '共青团员', '贵州贵阳', 85.3),
('2023010101', '杨帆', 2, '1390000121', 'yangfan@student.edu.cn', 2023, '计算机学院', '软件工程', '软工5班', 1, 1, '共青团员', '黑龙江哈尔滨', 86.8),
('2023010102', '胡云', 1, '1390000122', 'huyun@student.edu.cn', 2023, '信息学院', '电子信息工程', '电信3班', 1, 1, '共青团员', '吉林长春', 83.5),
('2023010103', '朱红', 2, '1390000123', 'zhuhong@student.edu.cn', 2023, '软件学院', '网络空间安全', '网安1班', 1, 1, '共青团员', '河北石家庄', 89.1),
('2023010104', '魏强', 1, '1390000124', 'weiqiang@student.edu.cn', 2023, '计算机学院', '物联网工程', '物联2班', 1, 1, '共青团员', '甘肃兰州', 84.6),
('2023010105', '韩梅', 2, '1390000125', 'hanmei@student.edu.cn', 2023, '软件学院', '软件工程', '软工6班', 1, 1, '共青团员', '海南海口', 87.2),
('2023010106', '李杰', 1, '1390000126', 'lijie@student.edu.cn', 2023, '计算机学院', '人工智能', '人智2班', 1, 1, '共青团员', '青海西宁', 90.5),
('2023010107', '谢芳', 2, '1390000127', 'xiefang@student.edu.cn', 2023, '信息学院', '通信工程', '通信2班', 1, 1, '共青团员', '宁夏银川', 85.9),
('2023010108', '曾伟', 1, '1390000128', 'zengwei@student.edu.cn', 2023, '软件学院', '数据科学', '数科2班', 1, 1, '共青团员', '新疆乌鲁木齐', 86.4),
('2023010109', '丁娜', 2, '1390000129', 'dingna@student.edu.cn', 2023, '计算机学院', '计算机科学与技术', '计科5班', 1, 1, '中共预备党员', '西藏拉萨', 91.2),
('2023010110', '丁志', 1, '1390000130', 'dingzhi@student.edu.cn', 2023, '软件学院', '软件工程', '软工7班', 1, 1, '共青团员', '内蒙古呼和浩特', 84.1),
('2024010101', '周杰', 1, '1390000131', 'zhoujie@student.edu.cn', 2024, '计算机学院', '软件工程', '软工8班', 1, 1, '共青团员', '天津和平', 82.3),
('2024010102', '吴敏', 2, '1390000132', 'wumin@student.edu.cn', 2024, '信息学院', '电子信息工程', '电信4班', 1, 1, '共青团员', '重庆江北', 85.6),
('2024010103', '郑浩', 1, '1390000133', 'zhenghao@student.edu.cn', 2024, '计算机学院', '网络工程', '网工3班', 1, 1, '共青团员', '四川绵阳', 83.8),
('2024010104', '王艳', 2, '1390000134', 'wangyan@student.edu.cn', 2024, '软件学院', '人工智能', '人智3班', 1, 1, '共青团员', '云南大理', 88.7),
('2024010105', '李强', 1, '1390000135', 'liqiang@student.edu.cn', 2024, '计算机学院', '信息安全', '信安3班', 1, 1, '共青团员', '湖北宜昌', 84.2),
('2024010106', '陈瑶', 2, '1390000136', 'chenyao@student.edu.cn', 2024, '软件学院', '软件工程', '软工9班', 1, 1, '共青团员', '贵州安顺', 86.9),
('2024010107', '张磊', 1, '1390000137', 'zhanglei@student.edu.cn', 2024, '计算机学院', '物联网工程', '物联3班', 1, 1, '共青团员', '陕西宝鸡', 82.5),
('2024010108', '刘燕', 2, '1390000138', 'liuyan@student.edu.cn', 2024, '信息学院', '自动化', '自动2班', 1, 1, '共青团员', '甘肃天水', 87.1),
('2024010109', '黄瑞', 1, '1390000139', 'huangrui@student.edu.cn', 2024, '软件学院', '数字媒体', '数媒2班', 1, 1, '共青团员', '广西桂林', 85.4),
('2024010110', '孙勇', 1, '1390000140', 'sunyong@student.edu.cn', 2024, '计算机学院', '计算机科学与技术', '计科6班', 1, 1, '共青团员', '湖南株洲', 83.6);

-- 继续插入更多学生...
INSERT INTO sys_student (student_no, name, gender, phone, email, enrollment_year, college, major, class_name, education_level, student_status, political_status, hometown, profile_score) VALUES
('2020010111', '周洋', 1, '1390000141', 'zhouyang@student.edu.cn', 2020, '计算机学院', '计算机科学与技术', '计科1班', 1, 1, '共青团员', '北京朝阳', 86.2),
('2020010112', '吴娟', 2, '1390000142', 'wujuan@student.edu.cn', 2020, '软件学院', '软件工程', '软工1班', 1, 1, '中共党员', '上海静安', 93.1),
('2020010113', '徐磊', 1, '1390000143', 'xulei@student.edu.cn', 2020, '信息学院', '电子信息工程', '电信1班', 1, 1, '共青团员', '广州天河', 84.8),
('2020010114', '马丽', 2, '1390000144', 'mali@student.edu.cn', 2020, '计算机学院', '软件工程', '软工2班', 1, 1, '共青团员', '深圳南山', 88.3),
('2021010106', '王鹏', 1, '1390000145', 'wangpeng@student.edu.cn', 2021, '软件学院', '网络工程', '网工1班', 1, 1, '共青团员', '成都武侯', 85.7),
('2021010107', '李霞', 2, '1390000146', 'lixia@student.edu.cn', 2021, '计算机学院', '物联网工程', '物联1班', 1, 1, '中共预备党员', '武汉洪山', 90.8),
('2021010108', '陈龙', 1, '1390000147', 'chenlong@student.edu.cn', 2021, '信息学院', '通信工程', '通信1班', 1, 1, '共青团员', '南京鼓楼', 83.4),
('2021010109', '赵娟', 2, '1390000148', 'zhaojuan@student.edu.cn', 2021, '软件学院', '数据科学', '数科1班', 1, 1, '共青团员', '杭州西湖', 87.6),
('2021010110', '孙超', 1, '1390000149', 'sunchao@student.edu.cn', 2021, '计算机学院', '人工智能', '人智1班', 1, 1, '共青团员', '西安雁塔', 91.9),
('2022010111', '周丽', 2, '1390000150', 'zhoulii@student.edu.cn', 2022, '计算机学院', '计算机科学与技术', '计科3班', 1, 1, '共青团员', '郑州金水', 86.5),
('2022010112', '吴涛', 1, '1390000151', 'wutao@student.edu.cn', 2022, '软件学院', '软件工程', '软工3班', 1, 1, '共青团员', '济南历下', 84.3),
('2022010113', '郑艳', 2, '1390000152', 'zhengyan@student.edu.cn', 2022, '信息学院', '电子信息工程', '电信2班', 1, 1, '中共预备党员', '厦门思明', 89.7),
('2022010114', '王凯', 1, '1390000153', 'wangkai@student.edu.cn', 2022, '计算机学院', '网络工程', '网工2班', 1, 1, '共青团员', '长沙岳麓', 82.9),
('2022010115', '李欢', 2, '1390000154', 'lihuan@student.edu.cn', 2022, '软件学院', '人工智能', '人智1班', 1, 1, '共青团员', '沈阳和平', 92.4),
('2023010111', '张倩', 2, '1390000155', 'zhangqian@student.edu.cn', 2023, '计算机学院', '软件工程', '软工5班', 1, 1, '共青团员', '哈尔滨南岗', 85.1),
('2023010112', '刘洋', 1, '1390000156', 'liuyang@student.edu.cn', 2023, '信息学院', '电子信息工程', '电信3班', 1, 1, '共青团员', '长春朝阳', 83.7),
('2023010113', '陈飞', 1, '1390000157', 'chenfei@student.edu.cn', 2023, '软件学院', '网络空间安全', '网安1班', 1, 1, '共青团员', '石家庄桥西', 87.8),
('2023010114', '周颖', 2, '1390000158', 'zhouying@student.edu.cn', 2023, '计算机学院', '物联网工程', '物联2班', 1, 1, '共青团员', '兰州城关', 86.3),
('2023010115', '徐静', 2, '1390000159', 'xujing@student.edu.cn', 2023, '软件学院', '软件工程', '软工6班', 1, 1, '中共预备党员', '海口龙华', 90.2),
('2023010116', '赵磊', 1, '1390000160', 'zhaoleii@student.edu.cn', 2023, '计算机学院', '人工智能', '人智2班', 1, 1, '共青团员', '西宁城西', 84.5),
('2024010111', '孙洁', 2, '1390000161', 'sunjie@student.edu.cn', 2024, '信息学院', '通信工程', '通信2班', 1, 1, '共青团员', '银川兴庆', 87.9),
('2024010112', '吴强', 1, '1390000162', 'wuqiang@student.edu.cn', 2024, '软件学院', '数据科学', '数科2班', 1, 1, '共青团员', '乌鲁木齐天沙', 82.8),
('2024010113', '王娜', 2, '1390000163', 'wangna@student.edu.cn', 2024, '计算机学院', '计算机科学与技术', '计科5班', 1, 1, '共青团员', '拉萨城关', 89.3),
('2024010114', '李勇', 1, '1390000164', 'liyong@student.edu.cn', 2024, '软件学院', '软件工程', '软工7班', 1, 1, '共青团员', '呼和浩特新城', 84.7),
('2024010115', '陈颖', 2, '1390000165', 'chenying@student.edu.cn', 2024, '计算机学院', '信息安全', '信安3班', 1, 1, '共青团员', '天津南开', 86.1),
('2020010115', '刘畅', 1, '1390000166', 'liuchang@student.edu.cn', 2020, '计算机学院', '计算机科学与技术', '计科2班', 1, 1, '中共党员', '北京丰台', 94.5),
('2020010116', '黄燕', 2, '1390000167', 'huangyan@student.edu.cn', 2020, '软件学院', '软件工程', '软工2班', 1, 1, '共青团员', '上海徐汇', 86.9),
('2020010117', '杨军', 1, '1390000168', 'yangjun@student.edu.cn', 2020, '信息学院', '自动化', '自动1班', 1, 1, '共青团员', '广州越秀', 83.6),
('2020010118', '赵丽', 2, '1390000169', 'zhaolii@student.edu.cn', 2020, '计算机学院', '物联网工程', '物联1班', 1, 1, '中共预备党员', '深圳福田', 91.7),
('2020010119', '孙浩', 1, '1390000170', 'sunhao2@student.edu.cn', 2020, '软件学院', '数字媒体', '数媒1班', 1, 1, '共青团员', '成都青羊', 85.4),
('2020010120', '周婷', 2, '1390000171', 'zhouting@student.edu.cn', 2020, '计算机学院', '软件工程', '软工3班', 1, 1, '共青团员', '武汉江汉', 88.1),
('2021010111', '吴欢', 1, '1390000172', 'wuhuan@student.edu.cn', 2021, '信息学院', '电子信息工程', '电信2班', 1, 1, '共青团员', '南京玄武', 84.2),
('2021010112', '郑刚', 1, '1390000173', 'zhenggang@student.edu.cn', 2021, '软件学院', '网络工程', '网工2班', 1, 1, '共青团员', '杭州滨江', 86.7),
('2021010113', '王秀', 2, '1390000174', 'wangxiu@student.edu.cn', 2021, '计算机学院', '人工智能', '人智2班', 1, 1, '中共预备党员', '西安碑林', 90.6),
('2021010114', '李倩', 2, '1390000175', 'liqian@student.edu.cn', 2021, '软件学院', '软件工程', '软工4班', 1, 1, '共青团员', '郑州中原', 85.3),
('2022010116', '陈浩', 1, '1390000176', 'chenhao@student.edu.cn', 2022, '计算机学院', '计算机科学与技术', '计科4班', 1, 1, '共青团员', '济南市中', 83.9),
('2022010117', '张敏', 2, '1390000177', 'zhangmin@student.edu.cn', 2022, '信息学院', '通信工程', '通信3班', 1, 1, '共青团员', '厦门湖里', 87.5),
('2022010118', '刘洋', 1, '1390000178', 'liuyang2@student.edu.cn', 2022, '软件学院', '数据科学', '数科2班', 1, 1, '共青团员', '长沙雨花', 84.8),
('2022010119', '黄静', 2, '1390000179', 'huangjing@student.edu.cn', 2022, '计算机学院', '物联网工程', '物联3班', 1, 1, '中共预备党员', '沈阳皇姑', 89.4),
('2022010120', '徐鹏', 1, '1390000180', 'xupeng@student.edu.cn', 2022, '软件学院', '软件工程', '软工5班', 1, 1, '共青团员', '哈尔滨道里', 86.2);

-- =============================================
-- 2. 成绩表 - 每个学生约5-8门课程成绩
-- =============================================
DROP TABLE IF EXISTS academic_score;
CREATE TABLE academic_score (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id      BIGINT NOT NULL,
    course_code     VARCHAR(30) NOT NULL,
    course_name     VARCHAR(100) NOT NULL,
    course_type     TINYINT,
    credit          DECIMAL(3,1),
    semester        VARCHAR(20) NOT NULL,
    score           DECIMAL(5,2),
    score_type      TINYINT DEFAULT 1,
    rank_in_class   INT,
    rank_in_major   INT,
    exam_type       TINYINT DEFAULT 1,
    is_challenge    TINYINT DEFAULT 0,
    gpa             DECIMAL(4,2),
    deleted         TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_semester (semester),
    UNIQUE KEY uk_student_course_semester (student_id, course_code, semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入成绩数据（示例）
INSERT INTO academic_score (student_id, course_code, course_name, course_type, credit, semester, score, rank_in_class, rank_in_major, gpa, is_challenge) VALUES
(1, 'CS101', '计算机导论', 1, 3.0, '2020-1', 92.5, 5, 28, 4.0, 0),
(1, 'MA101', '高等数学A', 1, 4.0, '2020-1', 88.0, 12, 45, 3.7, 0),
(1, 'CS102', '程序设计基础', 1, 3.0, '2020-1', 95.0, 2, 15, 4.0, 0),
(1, 'CS201', '数据结构', 1, 4.0, '2020-2', 89.5, 8, 32, 3.7, 0),
(1, 'CS202', '离散数学', 1, 3.0, '2020-2', 91.0, 6, 25, 4.0, 0),
(1, 'CS301', '操作系统', 1, 4.0, '2021-1', 87.0, 15, 52, 3.7, 0),
(1, 'CS302', '计算机网络', 1, 3.0, '2021-1', 90.5, 7, 30, 4.0, 0),
(1, 'CS401', '数据库原理', 1, 3.0, '2021-2', 93.0, 3, 18, 4.0, 0),
(2, 'CS101', '计算机导论', 1, 3.0, '2020-1', 96.0, 1, 5, 4.0, 0),
(2, 'MA101', '高等数学A', 1, 4.0, '2020-1', 94.5, 2, 8, 4.0, 0),
(2, 'CS102', '程序设计基础', 1, 3.0, '2020-1', 98.0, 1, 2, 4.0, 0),
(2, 'CS201', '数据结构', 1, 4.0, '2020-2', 95.5, 1, 5, 4.0, 0),
(2, 'CS301', '操作系统', 1, 4.0, '2021-1', 94.0, 2, 10, 4.0, 0),
(2, 'CS401', '数据库原理', 1, 3.0, '2021-2', 96.5, 1, 3, 4.0, 0),
(3, 'CS101', '计算机导论', 1, 3.0, '2020-1', 85.0, 20, 65, 3.5, 0),
(3, 'MA101', '高等数学A', 1, 4.0, '2020-1', 82.5, 25, 80, 3.5, 0),
(3, 'CS201', '数据结构', 1, 4.0, '2020-2', 86.0, 18, 60, 3.7, 0),
(3, 'CS301', '操作系统', 1, 4.0, '2021-1', 84.0, 22, 70, 3.5, 0),
(4, 'CS101', '计算机导论', 1, 3.0, '2020-1', 88.5, 10, 40, 3.7, 0),
(4, 'CS201', '数据结构', 1, 4.0, '2020-2', 90.0, 7, 28, 4.0, 0),
(4, 'CS301', '操作系统', 1, 4.0, '2021-1', 89.0, 10, 38, 3.7, 0),
(5, 'CS101', '计算机导论', 1, 3.0, '2020-1', 91.0, 7, 32, 4.0, 0),
(5, 'CS201', '数据结构', 1, 4.0, '2020-2', 93.5, 4, 18, 4.0, 0),
(5, 'CS301', '操作系统', 1, 4.0, '2021-1', 92.0, 5, 22, 4.0, 0),
(5, 'CS401', '数据库原理', 1, 3.0, '2021-2', 94.5, 2, 8, 4.0, 0),
(6, 'CS101', '计算机导论', 1, 3.0, '2021-1', 89.0, 8, 35, 3.7, 0),
(6, 'CS201', '数据结构', 1, 4.0, '2021-2', 90.5, 6, 25, 4.0, 0),
(6, 'CS301', '操作系统', 1, 4.0, '2022-1', 88.0, 12, 45, 3.7, 0),
(7, 'CS101', '计算机导论', 1, 3.0, '2021-1', 86.5, 15, 55, 3.5, 0),
(7, 'CS201', '数据结构', 1, 4.0, '2021-2', 85.0, 18, 62, 3.5, 0),
(7, 'CS301', '操作系统', 1, 4.0, '2022-1', 87.5, 14, 50, 3.7, 0),
(8, 'CS101', '计算机导论', 1, 3.0, '2021-1', 90.0, 5, 22, 4.0, 0),
(8, 'CS201', '数据结构', 1, 4.0, '2021-2', 91.5, 4, 18, 4.0, 0),
(8, 'CS301', '操作系统', 1, 4.0, '2022-1', 89.0, 8, 35, 3.7, 0),
(9, 'CS101', '计算机导论', 1, 3.0, '2021-1', 84.0, 22, 75, 3.5, 0),
(9, 'CS201', '数据结构', 1, 4.0, '2021-2', 83.5, 25, 82, 3.5, 0),
(9, 'CS301', '操作系统', 1, 4.0, '2022-1', 85.0, 20, 68, 3.5, 0),
(10, 'CS101', '计算机导论', 1, 3.0, '2021-1', 88.5, 10, 42, 3.7, 0),
(10, 'CS201', '数据结构', 1, 4.0, '2021-2', 90.0, 7, 30, 4.0, 0),
(10, 'CS301', '操作系统', 1, 4.0, '2022-1', 88.5, 11, 40, 3.7, 0),
(11, 'CS101', '计算机导论', 1, 3.0, '2022-1', 87.0, 12, 48, 3.7, 0),
(11, 'CS201', '数据结构', 1, 4.0, '2022-2', 88.5, 10, 42, 3.7, 0),
(12, 'CS101', '计算机导论', 1, 3.0, '2022-1', 85.5, 18, 65, 3.5, 0),
(12, 'CS201', '数据结构', 1, 4.0, '2022-2', 86.0, 15, 58, 3.7, 0),
(13, 'CS101', '计算机导论', 1, 3.0, '2022-1', 89.5, 8, 32, 3.7, 0),
(13, 'CS201', '数据结构', 1, 4.0, '2022-2', 91.0, 5, 22, 4.0, 0),
(14, 'CS101', '计算机导论', 1, 3.0, '2022-1', 83.0, 25, 85, 3.5, 0),
(14, 'CS201', '数据结构', 1, 4.0, '2022-2', 82.5, 28, 92, 3.5, 0),
(15, 'CS101', '计算机导论', 1, 3.0, '2022-1', 92.0, 3, 12, 4.0, 0),
(15, 'CS201', '数据结构', 1, 4.0, '2022-2', 94.5, 2, 8, 4.0, 0),
(15, 'CS301', '人工智能导论', 2, 3.0, '2023-1', 93.0, 4, 15, 4.0, 1),
(16, 'CS101', '计算机导论', 1, 3.0, '2022-1', 86.0, 15, 58, 3.5, 0),
(16, 'CS201', '数据结构', 1, 4.0, '2022-2', 87.5, 12, 48, 3.7, 0),
(17, 'CS101', '计算机导论', 1, 3.0, '2022-1', 88.0, 10, 40, 3.7, 0),
(17, 'CS201', '数据结构', 1, 4.0, '2022-2', 90.0, 6, 28, 4.0, 0),
(18, 'CS101', '计算机导论', 1, 3.0, '2022-1', 84.5, 22, 78, 3.5, 0),
(18, 'CS201', '数据结构', 1, 4.0, '2022-2', 86.0, 18, 62, 3.7, 0),
(19, 'CS101', '计算机导论', 1, 3.0, '2022-1', 87.5, 11, 45, 3.7, 0),
(19, 'CS201', '数据结构', 1, 4.0, '2022-2', 89.0, 9, 38, 3.7, 0),
(20, 'CS101', '计算机导论', 1, 3.0, '2022-1', 85.0, 20, 72, 3.5, 0),
(20, 'CS201', '数据结构', 1, 4.0, '2022-2', 86.5, 16, 58, 3.5, 0),
(21, 'CS101', '计算机导论', 1, 3.0, '2023-1', 86.5, 14, 52, 3.5, 0),
(21, 'CS201', '数据结构', 1, 4.0, '2023-2', 88.0, 12, 45, 3.7, 0),
(22, 'CS101', '计算机导论', 1, 3.0, '2023-1', 83.0, 28, 95, 3.5, 0),
(22, 'CS201', '数据结构', 1, 4.0, '2023-2', 84.5, 25, 85, 3.5, 0),
(23, 'CS101', '计算机导论', 1, 3.0, '2023-1', 89.0, 10, 38, 3.7, 0),
(23, 'CS201', '数据结构', 1, 4.0, '2023-2', 90.5, 7, 28, 4.0, 0),
(24, 'CS101', '计算机导论', 1, 3.0, '2023-1', 84.0, 25, 88, 3.5, 0),
(24, 'CS201', '数据结构', 1, 4.0, '2023-2', 86.0, 20, 68, 3.5, 0),
(25, 'CS101', '计算机导论', 1, 3.0, '2023-1', 87.0, 15, 55, 3.7, 0),
(25, 'CS201', '数据结构', 1, 4.0, '2023-2', 89.5, 10, 38, 3.7, 0),
(26, 'CS101', '计算机导论', 1, 3.0, '2023-1', 90.5, 6, 25, 4.0, 0),
(26, 'CS201', '数据结构', 1, 4.0, '2023-2', 92.0, 4, 15, 4.0, 0),
(26, 'CS301', '人工智能导论', 2, 3.0, '2024-1', 91.5, 5, 20, 4.0, 1),
(27, 'CS101', '计算机导论', 1, 3.0, '2023-1', 85.5, 20, 70, 3.5, 0),
(27, 'CS201', '数据结构', 1, 4.0, '2023-2', 87.0, 16, 58, 3.5, 0),
(28, 'CS101', '计算机导论', 1, 3.0, '2023-1', 86.0, 18, 62, 3.5, 0),
(28, 'CS201', '数据结构', 1, 4.0, '2023-2', 88.0, 14, 48, 3.7, 0),
(29, 'CS101', '计算机导论', 1, 3.0, '2023-1', 91.0, 4, 18, 4.0, 0),
(29, 'CS201', '数据结构', 1, 4.0, '2023-2', 93.5, 2, 8, 4.0, 0),
(30, 'CS101', '计算机导论', 1, 3.0, '2023-1', 84.0, 26, 90, 3.5, 0),
(30, 'CS201', '数据结构', 1, 4.0, '2023-2', 85.5, 22, 75, 3.5, 0);

-- =============================================
-- 3. 学生活动表
-- =============================================
DROP TABLE IF EXISTS student_activity;
CREATE TABLE student_activity (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id      BIGINT NOT NULL,
    activity_type   TINYINT NOT NULL,
    activity_name   VARCHAR(200) NOT NULL,
    organization    VARCHAR(200),
    role            VARCHAR(50),
    activity_date   DATE NOT NULL,
    semester        VARCHAR(20),
    description     TEXT,
    certificate_no  VARCHAR(100),
    level           TINYINT,
    award_type      TINYINT,
    award_name      VARCHAR(100),
    score_weight    DECIMAL(5,2) DEFAULT 0,
    evidence_url    VARCHAR(500),
    deleted         TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入活动数据
INSERT INTO student_activity (student_id, activity_type, activity_name, organization, role, activity_date, semester, level, award_type, award_name, score_weight) VALUES
(1, 1, '全国大学生程序设计竞赛', '教育部', '队长', '2022-10-15', '2022-2', 4, 2, 'ACM-ICPC区域赛银奖', 8.5),
(1, 3, '暑期社会实践', '校团委', '队长', '2023-07-01', '2023-0', 2, 4, '优秀社会实践团队', 7.0),
(1, 4, '学生会主席', '计算机学院', '主席', '2023-09-01', '2023-1', 2, 4, '优秀学生干部', 8.0),
(2, 1, '数学建模竞赛', '中国工业与应用数学学会', '队员', '2022-09-01', '2022-2', 4, 1, '国家一等奖', 9.5),
(2, 2, '科研项目', '计算机学院实验室', '负责人', '2023-03-01', '2023-1', 3, NULL, 'SCI论文发表', 9.0),
(2, 4, '班级学习委员', '辅导员办公室', '学习委员', '2022-09-01', '2022-1', 1, NULL, NULL, 6.0),
(3, 1, '网络安全竞赛', '信息安全协会', '队员', '2023-05-01', '2023-1', 3, 3, '省赛三等奖', 7.5),
(3, 5, '社区志愿服务', '校青年志愿者协会', '志愿者', '2023-03-05', '2023-0', 1, NULL, '志愿时长100小时', 6.0),
(4, 6, '校园歌手大赛', '校文化艺术中心', '参赛者', '2023-04-15', '2023-1', 2, 2, '校赛二等奖', 7.0),
(4, 5, '支教活动', '教育发展基金会', '教师', '2023-07-15', '2023-0', 3, NULL, '暑期支教优秀志愿者', 7.5),
(5, 1, '机器人竞赛', '机械工程学院', '队长', '2022-11-01', '2022-2', 4, 1, '国家一等奖', 9.0),
(5, 2, '大学生创新创业项目', '教务处', '负责人', '2023-01-01', '2023-0', 3, NULL, '国家级立项', 8.5),
(5, 4, '社团联合会主席', '校团委', '主席', '2022-09-01', '2022-1', 2, NULL, NULL, 8.0),
(6, 1, '数据结构竞赛', '计算机学院', '一等奖', '2023-04-01', '2023-1', 2, 1, '院赛一等奖', 8.0),
(6, 3, '企业实习', '腾讯科技', '实习生', '2023-07-01', '2023-0', 4, NULL, '优秀实习生', 8.0),
(7, 5, '迎新志愿者', '校青年志愿者协会', '组长', '2023-09-01', '2023-1', 1, NULL, NULL, 6.0),
(8, 1, 'ACM程序设计竞赛', '计算机学院', '队员', '2023-06-01', '2023-1', 3, 2, '省赛银奖', 8.0),
(8, 4, '班长', '软件学院', '班长', '2022-09-01', '2022-1', 1, NULL, '优秀班干部', 7.0),
(9, 3, '社会调研', '校社会科学部', '队长', '2023-08-01', '2023-0', 2, NULL, NULL, 6.5),
(10, 1, '数学竞赛', '数学学院', '参赛者', '2023-10-01', '2023-2', 3, 3, '省赛三等奖', 7.0);

-- =============================================
-- 4. 学业预警表
-- =============================================
DROP TABLE IF EXISTS academic_warning;
CREATE TABLE academic_warning (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id      BIGINT NOT NULL,
    warning_type    TINYINT NOT NULL,
    warning_level   TINYINT NOT NULL,
    warning_message TEXT,
    trigger_condition VARCHAR(500),
    suggest_action  TEXT,
    status          TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_warning_type (warning_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入预警数据
INSERT INTO academic_warning (student_id, warning_type, warning_level, warning_message, trigger_condition, suggest_action, status) VALUES
(14, 1, 2, '连续两学期数据结构成绩低于75分', '数据结构<75', '建议参加学业辅导', 0),
(22, 2, 1, '本学期必修课学分未修满', '必修学分<10', '建议补选课程', 0),
(30, 3, 1, '总学分差3分未达到毕业要求', '总学分不足', '建议重修或补修', 0),
(9, 1, 1, '操作系统课程成绩偏低', '操作系统<70', '建议参加补考辅导', 0);

-- =============================================
-- 5. 用户表
-- =============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL UNIQUE,
    password        VARCHAR(200) NOT NULL,
    real_name       VARCHAR(50),
    role            VARCHAR(20) DEFAULT 'student',
    phone           VARCHAR(20),
    email           VARCHAR(100),
    status          TINYINT DEFAULT 1,
    last_login_time DATETIME,
    last_login_ip   VARCHAR(50),
    deleted         TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户数据 (密码均为 123456)
INSERT INTO sys_user (username, password, real_name, role, phone, email, status) VALUES
('admin', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '系统管理员', 'admin', '13800000001', 'admin@edu.cn', 1),
('teacher001', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '张教务', 'teacher', '13800000002', 'zhangjw@edu.cn', 1),
('teacher002', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '李教务', 'teacher', '13800000003', 'lijw@edu.cn', 1),
('counselor001', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '王辅导员', 'counselor', '13800000004', 'wangxy@edu.cn', 1),
('counselor002', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '赵辅导员', 'counselor', '13800000005', 'zhaoxy@edu.cn', 1),
('counselor003', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '刘辅导员', 'counselor', '13800000006', 'liuyf@edu.cn', 1),
('student001', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '张伟', 'student', '1390000101', 'zhangwei@student.edu.cn', 1),
('student002', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '李娜', 'student', '1390000102', 'lina@student.edu.cn', 1),
('student003', '$2a$10$VsT7.MRNr9p0OCb0rWMB4eH8aJ1B9z0v0rX1K2Y0r1r0r1r0r1r', '王强', 'student', '1390000103', 'wangqiang@student.edu.cn', 1);

-- =============================================
-- 6. 课程知识域表
-- =============================================
DROP TABLE IF EXISTS course_knowledge_domain;
CREATE TABLE course_knowledge_domain (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code     VARCHAR(30) NOT NULL,
    course_name     VARCHAR(100) NOT NULL,
    knowledge_area  VARCHAR(50) NOT NULL,
    knowledge_point VARCHAR(100) NOT NULL,
    difficulty_level TINYINT DEFAULT 3,
    prerequisite    VARCHAR(500),
    deleted         TINYINT DEFAULT 0,
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_course_knowledge (course_code, knowledge_point)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO course_knowledge_domain (course_code, course_name, knowledge_area, knowledge_point, difficulty_level) VALUES
('CS101', '计算机导论', '计算机基础', '程序设计基础', 1),
('CS101', '计算机导论', '计算机基础', '计算机系统概述', 1),
('CS101', '计算机导论', '计算机基础', '数据结构与算法基础', 2),
('CS201', '数据结构', '计算机基础', '线性表', 3),
('CS201', '数据结构', '计算机基础', '栈和队列', 3),
('CS201', '数据结构', '计算机基础', '树和二叉树', 3),
('CS201', '数据结构', '计算机基础', '图', 4),
('CS201', '数据结构', '计算机基础', '查找和排序', 3),
('CS301', '操作系统', '系统基础', '进程管理', 4),
('CS301', '操作系统', '系统基础', '内存管理', 4),
('CS301', '操作系统', '系统基础', '文件系统', 4),
('CS302', '计算机网络', '网络基础', 'TCP/IP协议族', 4),
('CS302', '计算机网络', '网络基础', '网络层协议', 4),
('CS302', '计算机网络', '网络基础', '传输层协议', 4),
('CS401', '数据库原理', '数据管理', '关系数据库理论', 3),
('CS401', '数据库原理', '数据管理', 'SQL语言', 3),
('CS401', '数据库原理', '数据管理', '数据库设计', 3),
('MA101', '高等数学A', '数学基础', '极限与连续', 2),
('MA101', '高等数学A', '数学基础', '导数与微分', 2),
('MA101', '高等数学A', '数学基础', '积分学', 3);

-- =============================================
-- 查看统计
-- =============================================
SELECT '学生总数' as 项目, COUNT(*) as 数量 FROM sys_student
UNION ALL
SELECT '成绩记录数', COUNT(*) FROM academic_score
UNION ALL
SELECT '活动记录数', COUNT(*) FROM student_activity
UNION ALL
SELECT '预警记录数', COUNT(*) FROM academic_warning
UNION ALL
SELECT '用户数', COUNT(*) FROM sys_user;

-- =============================================
-- 6. 活动表 (用于活动管理)
-- =============================================
DROP TABLE IF EXISTS activity;
CREATE TABLE activity (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                VARCHAR(200) NOT NULL COMMENT '活动名称',
    activity_type       TINYINT COMMENT '活动类型: 1-竞赛, 2-科研, 3-社会实践, 4-学生工作, 5-志愿服务, 6-文体活动, 7-培训, 8-资格认证',
    category            VARCHAR(50) COMMENT '一级分类: 比赛获奖, 培训经历, 工作经历, 学生活动, 个人荣誉, 资格认证',
    sub_category        VARCHAR(50) COMMENT '二级分类: 创新创业, 科技创新, 劳动实践等',
    level               TINYINT COMMENT '活动级别: 1-院级, 2-校级, 3-省部级, 4-国家级, 5-国际级',
    organizer           VARCHAR(200) COMMENT '主办单位',
    co_organizer        VARCHAR(200) COMMENT '承办单位',
    description         TEXT COMMENT '活动描述',
    target_audience     VARCHAR(200) COMMENT '面向对象',
    register_start_time DATE COMMENT '报名开始时间',
    register_end_time   DATE COMMENT '报名截止时间',
    activity_start_time  DATE COMMENT '活动开始时间',
    activity_end_time   DATE COMMENT '活动结束时间',
    location            VARCHAR(200) COMMENT '活动地点',
    max_participants    INT DEFAULT 0 COMMENT '报名人数上限',
    current_participants INT DEFAULT 0 COMMENT '当前报名人数',
    reward              TEXT COMMENT '奖励/收获',
    requirements        TEXT COMMENT '参赛要求',
    contact             VARCHAR(100) COMMENT '联系方式',
    poster_url          VARCHAR(500) COMMENT '活动海报URL',
    detail_url          VARCHAR(500) COMMENT '活动详情URL',
    enable_recommend    TINYINT DEFAULT 0 COMMENT '是否开启推荐: 0-否, 1-是',
    recommend_weight   DECIMAL(5,2) DEFAULT 50 COMMENT '推荐权重',
    recommend_to_newbie TINYINT DEFAULT 0 COMMENT '是否推荐给新人',
    recommend_to_competition_type TINYINT DEFAULT 0 COMMENT '是否推荐给竞赛型学生',
    recommend_to_research_type   TINYINT DEFAULT 0 COMMENT '是否推荐给科研型学生',
    recommend_to_startup_type    TINYINT DEFAULT 0 COMMENT '是否推荐给创业型学生',
    status              VARCHAR(20) DEFAULT 'draft' COMMENT '状态: draft-草稿, published-已发布, ongoing-进行中, ended-已结束, cancelled-已取消',
    deleted             TINYINT DEFAULT 0,
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_level (level),
    INDEX idx_status (status),
    INDEX idx_enable_recommend (enable_recommend)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入活动测试数据
INSERT INTO activity (name, activity_type, category, sub_category, level, organizer, register_start_time, register_end_time, activity_start_time, activity_end_time, location, reward, enable_recommend, recommend_weight, recommend_to_newbie, recommend_to_competition_type, recommend_to_research_type, recommend_to_startup_type, status) VALUES
('全国大学生程序设计竞赛', 1, '比赛获奖', '科技创新', 4, '教育部高等学校计算机类专业教学指导委员会', '2024-03-01', '2024-03-15', '2024-04-01', '2024-04-03', '电子科技大学', '获得ACM金银铜牌证书，增加保研积分', 1, 90, 1, 1, 1, 1, 'published'),
('挑战杯全国大学生课外学术科技作品竞赛', 1, '比赛获奖', '学术研究', 4, '共青团中央', '2024-02-01', '2024-02-28', '2024-05-01', '2024-05-15', '待定', '国赛获奖可获得保研资格和创新创业学分', 1, 95, 1, 1, 1, 1, 'published'),
('大学生创新创业训练计划项目', 2, '培训经历', '创新创业', 3, '教育部高等教育司', '2024-01-01', '2024-03-30', '2024-04-01', '2024-12-31', '校内', '国家级立项可获得创新学分和科研经费支持', 1, 85, 1, 0, 0, 1, 'published'),
('暑期社会实践活动', 3, '学生活动', '劳动实践', 2, '校团委', '2024-05-01', '2024-06-20', '2024-07-01', '2024-08-31', '各地实践基地', '获得社会实践学分和志愿者时长', 1, 70, 1, 0, 0, 0, 'published'),
('全国大学生英语竞赛', 1, '比赛获奖', '学术研究', 4, '高等学校大学外语教学研究会', '2024-02-15', '2024-03-20', '2024-04-21', '2024-04-21', '各省级考点', '获奖可获得学分和奖学金评定资格', 1, 80, 1, 1, 1, 0, 'published'),
('机器人大赛', 1, '比赛获奖', '科技创新', 4, '中国自动化学会', '2024-03-15', '2024-04-30', '2024-07-15', '2024-07-20', '北京', '获奖可获得创新学分和技术认证', 1, 88, 1, 1, 1, 0, 'published'),
('职业生涯规划培训', 7, '培训经历', '技能培训', 2, '就业指导中心', '2024-03-01', '2024-03-10', '2024-03-15', '2024-03-16', '图书馆报告厅', '帮助学生明确职业发展方向', 1, 60, 1, 0, 0, 0, 'published'),
('专业技能证书考试', 8, '资格认证', '专业证书', 3, '工业和信息化部', '2024-04-01', '2024-04-15', '2024-05-01', '2024-05-01', '计算机学院', '获得行业认可的技能证书', 1, 75, 0, 1, 1, 0, 'published');

-- =============================================
-- 7. 活动推荐表
-- =============================================
DROP TABLE IF EXISTS activity_recommendation;
CREATE TABLE activity_recommendation (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id          BIGINT NOT NULL COMMENT '学生ID',
    student_no          VARCHAR(50) COMMENT '学生学号',
    activity_id         BIGINT NOT NULL COMMENT '活动ID',
    activity_name       VARCHAR(200) COMMENT '活动名称',
    activity_type       TINYINT COMMENT '活动类型',
    category            VARCHAR(50) COMMENT '分类',
    sub_category        VARCHAR(50) COMMENT '二级分类',
    recommend_score     DECIMAL(5,2) COMMENT '推荐分数: 0-100',
    recommend_reason    TEXT COMMENT '推荐理由',
    match_dimension     VARCHAR(50) COMMENT '匹配维度: profile_match-画像匹配, history_match-历史相似, similar_student-同类学生',
    level               VARCHAR(20) COMMENT '活动级别',
    organizer           VARCHAR(200) COMMENT '主办单位',
    register_start_time DATE COMMENT '报名开始时间',
    register_end_time   DATE COMMENT '报名截止时间',
    activity_start_time DATE COMMENT '活动开始时间',
    activity_end_time   DATE COMMENT '活动结束时间',
    location            VARCHAR(200) COMMENT '活动地点',
    reward              TEXT COMMENT '奖励/收获',
    is_registered      TINYINT DEFAULT 0 COMMENT '是否已报名: 0-否, 1-是',
    is_completed        TINYINT DEFAULT 0 COMMENT '是否已完成: 0-否, 1-是',
    status              VARCHAR(20) DEFAULT 'pending' COMMENT '推荐状态: pending-待处理, viewed-已查看, interested-感兴趣, applied-已报名, not_interested-不感兴趣',
    deleted             TINYINT DEFAULT 0,
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_activity_id (activity_id),
    INDEX idx_status (status),
    INDEX idx_recommend_score (recommend_score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- 8. 知识图谱表
-- =============================================
DROP TABLE IF EXISTS knowledge_graph;
CREATE TABLE knowledge_graph (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    head_type           VARCHAR(50) NOT NULL COMMENT '头实体类型: student, college, major, course, activity',
    head_id             VARCHAR(50) NOT NULL COMMENT '头实体ID',
    head_name           VARCHAR(100) COMMENT '头实体名称',
    relation_type       VARCHAR(50) NOT NULL COMMENT '关系类型: static_relation, dynamic_relation',
    relation            VARCHAR(100) NOT NULL COMMENT '一级关系: 隶属院系, 参加活动, 获得奖项等',
    sub_relation        VARCHAR(100) COMMENT '二级关系: 科技创新, 学术研究等',
    tail_type           VARCHAR(50) COMMENT '尾实体类型',
    tail_id             VARCHAR(50) COMMENT '尾实体ID',
    tail_name           VARCHAR(200) COMMENT '尾实体名称/成果名称',
    event_time          DATETIME COMMENT '发生时间',
    time_dimension      VARCHAR(20) COMMENT '时间维度值(如: 2022-05)',
    activity_level      VARCHAR(20) COMMENT '活动级别: 院级, 校级, 省部级, 国家级, 国际级',
    award_level         VARCHAR(20) COMMENT '奖项等级: 特等奖, 一等奖, 二等奖, 三等奖, 优秀奖, 参与奖',
    confidence          DECIMAL(5,2) DEFAULT 1.0 COMMENT '置信度: 0-1',
    source              VARCHAR(50) DEFAULT 'manual' COMMENT '来源: manual, system, knowledge_base',
    evidence            TEXT COMMENT '证据/备注',
    deleted             TINYINT DEFAULT 0,
    create_time         DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_head_id (head_id),
    INDEX idx_relation_type (relation_type),
    INDEX idx_relation (relation),
    INDEX idx_time_dimension (time_dimension)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入知识图谱测试数据（基于学生1的静态关系和活动）
INSERT INTO knowledge_graph (head_type, head_id, head_name, relation_type, relation, tail_type, tail_name) VALUES
('student', '2021001001', '张三', 'static_relation', '隶属院系', 'college', '计算机学院'),
('student', '2021001001', '张三', 'static_relation', '隶属专业', 'major', '计算机科学与技术'),
('student', '2021001001', '张三', 'static_relation', '性别', 'gender', '男'),
('student', '2021001001', '张三', 'static_relation', '年级', 'grade', '2021级'),
('student', '2021001001', '张三', 'static_relation', '班级', 'class', '计算机1班'),
('student', '2021001001', '张三', 'static_relation', '来自于', 'hometown', '四川省成都市');

INSERT INTO knowledge_graph (head_type, head_id, head_name, relation_type, relation, sub_relation, tail_type, tail_name, time_dimension, activity_level, award_level) VALUES
('student', '2021001001', '张三', 'dynamic_relation', '比赛获奖', '科技创新', 'activity', '全国大学生程序设计竞赛', '2022-10', '国家级', '银奖'),
('student', '2021001001', '张三', 'dynamic_relation', '比赛获奖', '学术研究', 'activity', 'ACM-ICPC区域赛银奖', '2022-10', '国家级', '银奖'),
('student', '2021001001', '张三', 'dynamic_relation', '培训经历', '技能培训', 'activity', '软件工程培训', '2023-01', '校级', NULL),
('student', '2021001001', '张三', 'dynamic_relation', '学生活动', '社团活动', 'activity', '学生会主席', '2023-09', '校级', NULL),
('student', '2021001001', '张三', 'dynamic_relation', '个人荣誉', '三好学生', 'activity', '校级三好学生', '2023-12', '校级', NULL),
('student', '2021001001', '张三', 'dynamic_relation', '比赛获奖', '创新创业', 'activity', '挑战杯银奖', '2024-05', '国家级', '银奖');

-- 更新统计查询
SELECT '学生总数' as 项目, COUNT(*) as 数量 FROM sys_student
UNION ALL
SELECT '成绩记录数', COUNT(*) FROM academic_score
UNION ALL
SELECT '活动记录数', COUNT(*) FROM student_activity
UNION ALL
SELECT '预警记录数', COUNT(*) FROM academic_warning
UNION ALL
SELECT '活动管理记录数', COUNT(*) FROM activity
UNION ALL
SELECT '活动推荐记录数', COUNT(*) FROM activity_recommendation
UNION ALL
SELECT '知识图谱记录数', COUNT(*) FROM knowledge_graph
UNION ALL
SELECT '用户数', COUNT(*) FROM sys_user;
