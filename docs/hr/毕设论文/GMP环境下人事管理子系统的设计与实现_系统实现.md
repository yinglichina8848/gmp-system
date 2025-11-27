# GMP环境下人事管理子系统的设计与实现
## 系统实现

## 📋 文档信息

| 属性 | 值 |
|------|---|
| 文档标题 | GMP环境下人事管理子系统的设计与实现_系统实现 |
| 版本号 | v0.1.0-draft |
| 创建日期 | 2025年11月21日 |
| 更新日期 | 2025年11月21日 |
| 作者 | 毕业设计团队 |
| 状态 | 草稿 |

## 1️⃣ 系统实现概述

GMP环境下人事管理子系统采用前后端分离的微服务架构，后端基于Spring Boot + Spring Cloud构建微服务体系，前端基于React + Ant Design实现用户界面。系统严格遵循GMP合规要求，实现了完整的人事管理功能，包括组织架构管理、员工信息管理、考勤管理、培训管理、资质证书管理和GMP合规性管理等核心模块。

本章将详细介绍系统的实现过程，包括技术架构实现、核心功能模块实现、数据库实现、安全实现和GMP合规特性实现等内容。

## 2️⃣ 技术架构实现

### 2.1 微服务框架搭建

#### 2.1.1 服务注册与发现

系统使用Nacos作为服务注册与发现中心，实现微服务的注册、发现和健康检查。

**Nacos配置示例**：

```yaml
# bootstrap.yml
spring:
  application:
    name: employee-service
  cloud:
    nacos:
      discovery:
        server-addr: nacos-server:8848
        namespace: gmp-hr-system
        group: GMP_HR_GROUP
      config:
        server-addr: nacos-server:8848
        namespace: gmp-hr-system
        group: GMP_HR_GROUP
        file-extension: yaml
```

**服务注册代码示例**：

```java
@SpringBootApplication
@EnableDiscoveryClient
public class EmployeeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
```

#### 2.1.2 API网关实现

系统使用Spring Cloud Gateway作为API网关，负责请求路由、权限验证、负载均衡和限流等功能。

**Gateway配置示例**：

```yaml
# gateway.yml
spring:
  cloud:
    gateway:
      routes:
        - id: employee-service
          uri: lb://employee-service
          predicates:
            - Path=/api/v1/employees/**
          filters:
            - StripPrefix=1
            - AuthFilter
        - id: training-service
          uri: lb://training-service
          predicates:
            - Path=/api/v1/trainings/**
          filters:
            - StripPrefix=1
            - AuthFilter
```

**自定义认证过滤器**：

```java
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 实现JWT令牌验证逻辑
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        try {
            // 验证JWT令牌
            String jwt = token.substring(7);
            Claims claims = Jwts.parser().setSigningKey("secretKey")
                .parseClaimsJws(jwt).getBody();
            
            // 将用户信息添加到请求头
            exchange.getRequest().mutate()
                .header("user-id", claims.get("userId").toString())
                .header("username", claims.get("username").toString())
                .build();
                
            return chain.filter(exchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
}
```

#### 2.1.3 配置中心

系统使用Nacos Config作为配置中心，实现配置的集中管理和动态更新。

**配置示例**：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://mysql-server:3306/gmp_hr_employee?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

# 缓存配置
redis:
  host: redis-server
  port: 6379
  database: 0
  timeout: 3000

# 日志配置
logging:
  level:
    com.gmp.hr: info
    org.springframework: warn
```

### 2.2 数据库实现

#### 2.2.1 数据库初始化脚本

系统使用MySQL作为主数据库，以下是核心表的创建脚本示例：

**员工表创建脚本**：

```sql
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `employee_code` varchar(50) NOT NULL COMMENT '员工工号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `gender` varchar(10) NOT NULL COMMENT '性别',
  `birth_date` date NOT NULL COMMENT '出生日期',
  `id_card_no` varchar(20) NOT NULL COMMENT '身份证号',
  `phone_number` varchar(20) NOT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `entry_date` date NOT NULL COMMENT '入职日期',
  `status` varchar(20) NOT NULL COMMENT '状态（在职/离职/试用等）',
  `department_id` bigint NOT NULL COMMENT '部门ID',
  `position_id` bigint NOT NULL COMMENT '岗位ID',
  `created_by` varchar(50) NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_by` varchar(50) NOT NULL COMMENT '更新人',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_code` (`employee_code`),
  UNIQUE KEY `uk_id_card_no` (`id_card_no`),
  UNIQUE KEY `uk_phone_number` (`phone_number`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_position_id` (`position_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_employee_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `fk_employee_position` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';
```

**审计日志表创建脚本**：

```sql
CREATE TABLE `audit_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  `ip_address` varchar(50) NOT NULL COMMENT 'IP地址',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `module_name` varchar(100) NOT NULL COMMENT '模块名称',
  `business_id` varchar(100) DEFAULT NULL COMMENT '业务ID',
  `old_value` text COMMENT '操作前值',
  `new_value` text COMMENT '操作后值',
  `operation_result` varchar(20) NOT NULL COMMENT '操作结果（成功/失败）',
  `error_message` text COMMENT '错误信息',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_module_name` (`module_name`),
  KEY `idx_business_id` (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';
```

#### 2.2.2 MyBatis-Plus配置

系统使用MyBatis-Plus作为ORM框架，以下是配置示例：

```java
@Configuration
@MapperScan("com.gmp.hr.employee.repository")
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
    
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setCacheEnabled(true);
        };
    }
}
```

### 2.3 前端架构实现

#### 2.3.1 React应用初始化

系统前端基于React 18构建，使用Vite作为构建工具，以下是项目结构示例：

```
├── public
├── src
│   ├── assets          # 静态资源
│   ├── components      # 通用组件
│   ├── config          # 配置文件
│   ├── pages           # 页面组件
│   │   ├── employee    # 员工管理页面
│   │   ├── department  # 部门管理页面
│   │   ├── training    # 培训管理页面
│   │   ├── attendance  # 考勤管理页面
│   │   └── certificate # 证书管理页面
│   ├── services        # API服务
│   ├── store           # Redux状态管理
│   ├── utils           # 工具函数
│   ├── App.jsx         # 应用主组件
│   ├── main.jsx        # 应用入口
│   └── routes.jsx      # 路由配置
├── .env                # 环境变量
├── package.json        # 项目依赖
└── vite.config.js      # Vite配置
```

**应用入口代码**：

```jsx
import React from 'react'
import ReactDOM from 'react-dom/client'
import { Provider } from 'react-redux'
import { BrowserRouter } from 'react-router-dom'
import { ConfigProvider } from 'antd'
import zhCN from 'antd/locale/zh_CN'
import store from './store'
import App from './App'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <Provider store={store}>
      <ConfigProvider locale={zhCN}>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </ConfigProvider>
    </Provider>
  </React.StrictMode>
)
```

#### 2.3.2 路由配置

系统使用React Router实现前端路由，以下是配置示例：

```jsx
import { lazy, Suspense } from 'react'
import { createBrowserRouter } from 'react-router-dom'
import Layout from './components/Layout'
import Login from './pages/Login'
import NotFound from './pages/NotFound'
import Loading from './components/Loading'

// 懒加载页面组件
const EmployeeList = lazy(() => import('./pages/employee/List'))
const EmployeeForm = lazy(() => import('./pages/employee/Form'))
const DepartmentList = lazy(() => import('./pages/department/List'))
const TrainingList = lazy(() => import('./pages/training/List'))
const AttendanceList = lazy(() => import('./pages/attendance/List'))
const CertificateList = lazy(() => import('./pages/certificate/List'))
const AuditLogList = lazy(() => import('./pages/audit/List'))

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />
  },
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        path: '',
        element: <EmployeeList />
      },
      {
        path: 'employees',
        element: (
          <Suspense fallback={<Loading />}>
            <EmployeeList />
          </Suspense>
        )
      },
      {
        path: 'employees/add',
        element: (
          <Suspense fallback={<Loading />}>
            <EmployeeForm />
          </Suspense>
        )
      },
      {
        path: 'employees/:id/edit',
        element: (
          <Suspense fallback={<Loading />}>
            <EmployeeForm />
          </Suspense>
        )
      },
      // 其他路由配置...
    ]
  },
  {
    path: '*',
    element: <NotFound />
  }
])

export default router
```

## 3️⃣ 核心功能模块实现

### 3.1 组织架构管理模块实现

#### 3.1.1 部门管理功能

**部门实体类**：

```java
@Data
@TableName("department")
public class Department extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotBlank(message = "部门编码不能为空")
    @TableField("department_code")
    private String departmentCode;
    
    @NotBlank(message = "部门名称不能为空")
    @TableField("department_name")
    private String departmentName;
    
    @TableField("parent_id")
    private Long parentId;
    
    @TableField("level")
    private Integer level;
    
    @TableField("description")
    private String description;
    
    @TableField("gmp_area")
    private String gmpArea;
}
```

**部门Service实现**：

```java
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Department createDepartment(Department department) {
        // 设置部门级别
        if (department.getParentId() == null) {
            // 顶级部门
            department.setLevel(1);
        } else {
            // 非顶级部门，获取父部门级别
            Department parent = departmentRepository.getById(department.getParentId());
            if (parent == null) {
                throw new BusinessException("父部门不存在");
            }
            department.setLevel(parent.getLevel() + 1);
        }
        
        // 保存部门
        departmentRepository.save(department);
        return department;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Department updateDepartment(Long id, Department department) {
        Department existing = departmentRepository.getById(id);
        if (existing == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 检查是否修改了父部门
        if (!Objects.equals(department.getParentId(), existing.getParentId())) {
            // 检查是否存在循环引用
            checkCircularReference(department.getParentId(), id);
            
            // 更新部门级别
            if (department.getParentId() == null) {
                department.setLevel(1);
            } else {
                Department parent = departmentRepository.getById(department.getParentId());
                if (parent == null) {
                    throw new BusinessException("父部门不存在");
                }
                department.setLevel(parent.getLevel() + 1);
            }
            
            // 更新子部门级别
            updateChildrenLevel(id, department.getLevel());
        }
        
        // 更新部门信息
        BeanUtils.copyProperties(department, existing, "id", "createdBy", "createdTime");
        departmentRepository.updateById(existing);
        return existing;
    }
    
    @Override
    public List<Department> getDepartmentTree() {
        List<Department> departments = departmentRepository.list();
        return buildDepartmentTree(departments, null);
    }
    
    // 构建部门树
    private List<Department> buildDepartmentTree(List<Department> departments, Long parentId) {
        List<Department> tree = new ArrayList<>();
        for (Department department : departments) {
            if (Objects.equals(department.getParentId(), parentId)) {
                department.setChildren(buildDepartmentTree(departments, department.getId()));
                tree.add(department);
            }
        }
        return tree;
    }
    
    // 检查循环引用
    private void checkCircularReference(Long parentId, Long departmentId) {
        if (parentId == null) {
            return;
        }
        
        Department parent = departmentRepository.getById(parentId);
        if (Objects.equals(parent.getId(), departmentId)) {
            throw new BusinessException("不能将部门设置为自身或其子孙部门的子部门");
        }
        
        checkCircularReference(parent.getParentId(), departmentId);
    }
    
    // 更新子部门级别
    private void updateChildrenLevel(Long parentId, Integer parentLevel) {
        List<Department> children = departmentRepository.findByParentId(parentId);
        for (Department child : children) {
            child.setLevel(parentLevel + 1);
            departmentRepository.updateById(child);
            // 递归更新子部门
            updateChildrenLevel(child.getId(), child.getLevel());
        }
    }
}
```

**部门Controller**：

```java
@RestController
@RequestMapping("/api/v1/departments")
@Api(tags = "部门管理")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    
    @PostMapping
    @ApiOperation("创建部门")
    public ResponseEntity<Department> createDepartment(@RequestBody @Valid Department department) {
        Department created = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新部门")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody @Valid Department department) {
        Department updated = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除部门")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    @ApiOperation("获取部门列表")
    public ResponseEntity<List<Department>> getDepartments() {
        List<Department> departments = departmentService.getDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/tree")
    @ApiOperation("获取部门树")
    public ResponseEntity<List<Department>> getDepartmentTree() {
        List<Department> tree = departmentService.getDepartmentTree();
        return ResponseEntity.ok(tree);
    }
}
```

### 3.2 员工信息管理模块实现

#### 3.2.1 员工基本信息管理

**员工实体类**：

```java
@Data
@TableName("employee")
public class Employee extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotBlank(message = "员工工号不能为空")
    @TableField("employee_code")
    private String employeeCode;
    
    @NotBlank(message = "姓名不能为空")
    @TableField("name")
    private String name;
    
    @NotBlank(message = "性别不能为空")
    @TableField("gender")
    private String gender;
    
    @NotNull(message = "出生日期不能为空")
    @TableField("birth_date")
    private Date birthDate;
    
    @NotBlank(message = "身份证号不能为空")
    @TableField("id_card_no")
    private String idCardNo;
    
    @NotBlank(message = "手机号不能为空")
    @TableField("phone_number")
    private String phoneNumber;
    
    @Email(message = "邮箱格式不正确")
    @TableField("email")
    private String email;
    
    @NotNull(message = "入职日期不能为空")
    @TableField("entry_date")
    private Date entryDate;
    
    @NotBlank(message = "状态不能为空")
    @TableField("status")
    private String status;
    
    @NotNull(message = "部门ID不能为空")
    @TableField("department_id")
    private Long departmentId;
    
    @NotNull(message = "岗位ID不能为空")
    @TableField("position_id")
    private Long positionId;
    
    // 扩展字段
    @TableField(exist = false)
    private String departmentName;
    
    @TableField(exist = false)
    private String positionName;
}
```

**员工Service实现**：

```java
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private QualificationVerificationService qualificationVerificationService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee createEmployee(Employee employee) {
        // 验证部门和岗位是否存在
        validateDepartmentAndPosition(employee.getDepartmentId(), employee.getPositionId());
        
        // 检查工号是否重复
        if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new BusinessException("员工工号已存在");
        }
        
        // 保存员工信息
        employeeRepository.save(employee);
        
        // GMP合规性检查
        qualificationVerificationService.checkEmployeeQualification(employee.getId());
        
        return employee;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = employeeRepository.getById(id);
        if (existing == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 验证部门和岗位是否存在
        validateDepartmentAndPosition(employee.getDepartmentId(), employee.getPositionId());
        
        // 检查工号是否重复
        if (!Objects.equals(employee.getEmployeeCode(), existing.getEmployeeCode()) &&
            employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new BusinessException("员工工号已存在");
        }
        
        // 更新员工信息
        BeanUtils.copyProperties(employee, existing, "id", "createdBy", "createdTime");
        employeeRepository.updateById(existing);
        
        // 如果员工状态或岗位发生变化，重新进行GMP合规性检查
        if (!Objects.equals(employee.getStatus(), existing.getStatus()) ||
            !Objects.equals(employee.getPositionId(), existing.getPositionId())) {
            qualificationVerificationService.checkEmployeeQualification(existing.getId());
        }
        
        return existing;
    }
    
    @Override
    public Page<Employee> queryEmployees(EmployeeQuery query) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        
        // 条件查询
        if (StringUtils.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(Employee::getEmployeeCode, query.getKeyword())
                            .or().like(Employee::getName, query.getKeyword())
                            .or().like(Employee::getPhoneNumber, query.getKeyword()));
        }
        
        if (query.getDepartmentId() != null) {
            wrapper.eq(Employee::getDepartmentId, query.getDepartmentId());
        }
        
        if (query.getPositionId() != null) {
            wrapper.eq(Employee::getPositionId, query.getPositionId());
        }
        
        if (StringUtils.isNotBlank(query.getStatus())) {
            wrapper.eq(Employee::getStatus, query.getStatus());
        }
        
        if (query.getEntryDateStart() != null) {
            wrapper.ge(Employee::getEntryDate, query.getEntryDateStart());
        }
        
        if (query.getEntryDateEnd() != null) {
            wrapper.le(Employee::getEntryDate, query.getEntryDateEnd());
        }
        
        // 排序
        wrapper.orderByDesc(Employee::getCreatedTime);
        
        // 分页查询
        Page<Employee> page = employeeRepository.page(new Page<>(query.getPage(), query.getSize()), wrapper);
        
        // 填充部门和岗位信息
        fillDepartmentAndPositionInfo(page.getRecords());
        
        return page;
    }
    
    private void validateDepartmentAndPosition(Long departmentId, Long positionId) {
        // 验证部门是否存在
        Department department = departmentRepository.getById(departmentId);
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 验证岗位是否存在且属于指定部门
        Position position = positionRepository.getById(positionId);
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        
        if (!Objects.equals(position.getDepartmentId(), departmentId)) {
            throw new BusinessException("岗位不属于指定部门");
        }
    }
    
    private void fillDepartmentAndPositionInfo(List<Employee> employees) {
        for (Employee employee : employees) {
            Department department = departmentRepository.getById(employee.getDepartmentId());
            if (department != null) {
                employee.setDepartmentName(department.getDepartmentName());
            }
            
            Position position = positionRepository.getById(employee.getPositionId());
            if (position != null) {
                employee.setPositionName(position.getPositionName());
            }
        }
    }
}
```

### 3.3 GMP合规性管理模块实现

#### 3.3.1 动态权限控制实现

**动态权限服务实现**：

```java
@Service
@Slf4j
public class DynamicPermissionServiceImpl implements DynamicPermissionService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private TrainingRecordRepository trainingRecordRepository;
    
    @Autowired
    private QualificationCertificateRepository certificateRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustPermissionsByQualification(Long employeeId) {
        // 获取员工信息
        Employee employee = employeeRepository.getById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 获取员工对应的用户
        User user = userRepository.findByEmployeeId(employeeId);
        if (user == null) {
            log.warn("员工[{}]没有对应的用户账号", employee.getEmployeeCode());
            return;
        }
        
        // 获取员工岗位信息
        Position position = positionRepository.getById(employee.getPositionId());
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        
        // 检查员工是否具备岗位所需的培训
        List<Long> requiredCourseIds = positionRequiredCourseRepository.findByPositionId(position.getId())
                .stream().map(PositionRequiredCourse::getCourseId).collect(Collectors.toList());
        
        boolean hasAllRequiredTrainings = true;
        for (Long courseId : requiredCourseIds) {
            TrainingRecord record = trainingRecordRepository.findByEmployeeIdAndCourseId(employeeId, courseId);
            if (record == null || !"通过".equals(record.getResult())) {
                hasAllRequiredTrainings = false;
                break;
            }
        }
        
        // 检查员工是否具备岗位所需的资质证书
        List<String> requiredCertificateTypes = positionRequiredCertificateRepository.findByPositionId(position.getId())
                .stream().map(PositionRequiredCertificate::getCertificateType).collect(Collectors.toList());
        
        boolean hasAllRequiredCertificates = true;
        for (String certificateType : requiredCertificateTypes) {
            QualificationCertificate certificate = certificateRepository
                    .findByEmployeeIdAndCertificateTypeAndStatus(employeeId, certificateType, "有效");
            if (certificate == null) {
                hasAllRequiredCertificates = false;
                break;
            }
        }
        
        // 动态调整权限
        if (hasAllRequiredTrainings && hasAllRequiredCertificates) {
            // 员工具备完整资质，授予GMP操作权限
            grantGmpOperationPermission(user.getId(), position.getGmpResponsibilities());
        } else {
            // 员工资质不完整，撤销GMP操作权限
            revokeGmpOperationPermission(user.getId());
            
            // 记录合规性警告
            complianceWarningService.createWarning(employeeId, "员工资质不完整", 
                "员工缺少必要的培训或证书，无法执行GMP操作");
        }
    }
    
    private void grantGmpOperationPermission(Long userId, String gmpResponsibilities) {
        // 解析GMP职责，确定需要授予的权限
        List<String> permissionCodes = parseGmpResponsibilities(gmpResponsibilities);
        
        // 授予权限（通过角色关联）
        for (String permissionCode : permissionCodes) {
            Role role = roleRepository.findByRoleCode("GMP_ROLE_" + permissionCode);
            if (role != null && !userRoleRepository.existsByUserIdAndRoleId(userId, role.getId())) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(role.getId());
                userRoleRepository.save(userRole);
            }
        }
    }
    
    private void revokeGmpOperationPermission(Long userId) {
        // 撤销所有GMP相关角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        for (UserRole userRole : userRoles) {
            Role role = roleRepository.getById(userRole.getRoleId());
            if (role != null && role.getRoleCode().startsWith("GMP_ROLE_")) {
                userRoleRepository.delete(userRole);
            }
        }
    }
    
    private List<String> parseGmpResponsibilities(String responsibilities) {
        // 解析职责字符串，提取权限代码
        List<String> permissionCodes = new ArrayList<>();
        if (StringUtils.isNotBlank(responsibilities)) {
            // 示例：从职责字符串中提取权限代码
            // 实际实现根据具体业务逻辑
            if (responsibilities.contains("生产操作")) {
                permissionCodes.add("PRODUCTION_OPERATION");
            }
            if (responsibilities.contains("质量检验")) {
                permissionCodes.add("QUALITY_INSPECTION");
            }
            if (responsibilities.contains("设备维护")) {
                permissionCodes.add("EQUIPMENT_MAINTENANCE");
            }
        }
        return permissionCodes;
    }
}
```

#### 3.3.2 审计日志实现

**审计日志切面**：

```java
@Aspect
@Component
@Slf4j
public class AuditLogAspect {
    @Autowired
    private AuditLogService auditLogService;
    
    @Autowired
    private HttpServletRequest request;
    
    // 拦截所有Controller方法
    @Around("execution(* com.gmp.hr.*.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取操作信息
        AuditLog auditLog = new AuditLog();
        
        // 设置操作人信息（从JWT中获取）
        String userId = request.getHeader("user-id");
        String username = request.getHeader("username");
        auditLog.setUserId(userId != null ? Long.parseLong(userId) : null);
        auditLog.setUsername(username != null ? username : "匿名用户");
        
        // 设置操作时间
        auditLog.setOperationTime(new Date());
        
        // 设置IP地址
        auditLog.setIpAddress(getClientIp(request));
        
        // 设置操作类型和模块名称
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 从注解中获取模块信息
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            auditLog.setOperationType(apiOperation.value());
        }
        
        Api api = joinPoint.getTarget().getClass().getAnnotation(Api.class);
        if (api != null && api.tags().length > 0) {
            auditLog.setModuleName(api.tags()[0]);
        }
        
        // 记录请求参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            // 只记录第一个参数作为业务参数
            Object businessArg = args[0];
            if (businessArg instanceof HttpServletRequest || 
                businessArg instanceof HttpServletResponse ||
                businessArg instanceof BindingResult) {
                // 忽略这些类型的参数
            } else {
                try {
                    auditLog.setNewValue(JSONObject.toJSONString(businessArg));
                } catch (Exception e) {
                    log.warn("参数序列化失败", e);
                }
            }
        }
        
        // 执行原方法
        Object result;
        try {
            result = joinPoint.proceed();
            auditLog.setOperationResult("成功");
            
            // 如果是更新操作，尝试获取更新前的值
            String methodName = method.getName();
            if (methodName.startsWith("update") && args.length > 0 && args[0] instanceof Long) {
                // 获取业务ID
                auditLog.setBusinessId(args[0].toString());
                
                // 尝试获取更新前的值（通过反射调用getById方法）
                try {
                    Object oldValue = getEntityById(joinPoint.getTarget(), (Long) args[0]);
                    if (oldValue != null) {
                        auditLog.setOldValue(JSONObject.toJSONString(oldValue));
                    }
                } catch (Exception e) {
                    log.warn("获取更新前值失败", e);
                }
            } else if (methodName.startsWith("create") && result != null) {
                // 创建操作，设置业务ID
                try {
                    Method getIdMethod = result.getClass().getMethod("getId");
                    Object id = getIdMethod.invoke(result);
                    if (id != null) {
                        auditLog.setBusinessId(id.toString());
                    }
                } catch (Exception e) {
                    log.warn("获取业务ID失败", e);
                }
            }
        } catch (Exception e) {
            auditLog.setOperationResult("失败");
            auditLog.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            // 异步保存审计日志
            saveAuditLog(auditLog);
        }
        
        return result;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    private Object getEntityById(Object target, Long id) throws Exception {
        // 通过反射调用getById方法
        Method getByIdMethod = findGetByIdMethod(target.getClass());
        if (getByIdMethod != null) {
            return getByIdMethod.invoke(target, id);
        }
        return null;
    }
    
    private Method findGetByIdMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("getById") && method.getParameterCount() == 1 && 
                method.getParameterTypes()[0] == Long.class) {
                method.setAccessible(true);
                return method;
            }
        }
        // 查找父类
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            return findGetByIdMethod(clazz.getSuperclass());
        }
        return null;
    }
    
    @Async
    private void saveAuditLog(AuditLog auditLog) {
        try {
            auditLogService.saveAuditLog(auditLog);
        } catch (Exception e) {
            log.error("保存审计日志失败", e);
        }
    }
}
```

### 3.4 前端组件实现

#### 3.4.1 员工管理页面

**员工列表组件**：

```jsx
import React, { useState, useEffect } from 'react'
import { Table, Button, Input, Select, DatePicker, Space, Popconfirm, message } from 'antd'
import { SearchOutlined, PlusOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { getEmployees, deleteEmployee } from '@/services/employeeService'
import moment from 'moment'

const { Option } = Select
const { RangePicker } = DatePicker

const EmployeeList = () => {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [data, setData] = useState([])
  const [total, setTotal] = useState(0)
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10
  })
  const [filters, setFilters] = useState({
    keyword: '',
    departmentId: undefined,
    positionId: undefined,
    status: undefined,
    entryDateRange: undefined
  })
  
  // 加载数据
  const loadData = async (params = {}) => {
    setLoading(true)
    try {
      const query = {
        page: params.current || pagination.current,
        size: params.pageSize || pagination.pageSize,
        ...filters
      }
      
      // 转换日期格式
      if (query.entryDateRange && query.entryDateRange.length === 2) {
        query.entryDateStart = query.entryDateRange[0].format('YYYY-MM-DD')
        query.entryDateEnd = query.entryDateRange[1].format('YYYY-MM-DD')
        delete query.entryDateRange
      }
      
      const response = await getEmployees(query)
      setData(response.data.records)
      setTotal(response.data.total)
      setPagination({
        ...pagination,
        current: response.data.current,
        pageSize: response.data.size
      })
    } catch (error) {
      message.error('加载数据失败')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }
  
  // 初始加载
  useEffect(() => {
    loadData()
  }, [])
  
  // 搜索
  const handleSearch = () => {
    loadData({ current: 1 })
  }
  
  // 重置
  const handleReset = () => {
    setFilters({
      keyword: '',
      departmentId: undefined,
      positionId: undefined,
      status: undefined,
      entryDateRange: undefined
    })
    loadData({ current: 1 })
  }
  
  // 删除员工
  const handleDelete = async (id) => {
    try {
      await deleteEmployee(id)
      message.success('删除成功')
      loadData()
    } catch (error) {
      message.error('删除失败')
      console.error(error)
    }
  }
  
  // 编辑员工
  const handleEdit = (id) => {
    navigate(`/employees/${id}/edit`)
  }
  
  // 添加员工
  const handleAdd = () => {
    navigate('/employees/add')
  }
  
  // 分页变化
  const handlePageChange = (page, pageSize) => {
    loadData({ current: page, pageSize })
  }
  
  // 表格列定义
  const columns = [
    {
      title: '员工工号',
      dataIndex: 'employeeCode',
      key: 'employeeCode'
    },
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name'
    },
    {
      title: '性别',
      dataIndex: 'gender',
      key: 'gender'
    },
    {
      title: '部门',
      dataIndex: 'departmentName',
      key: 'departmentName'
    },
    {
      title: '岗位',
      dataIndex: 'positionName',
      key: 'positionName'
    },
    {
      title: '入职日期',
      dataIndex: 'entryDate',
      key: 'entryDate',
      render: text => text ? moment(text).format('YYYY-MM-DD') : ''
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status'
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record.id)}>
            编辑
          </Button>
          <Popconfirm
            title="确定要删除该员工吗？"
            onConfirm={() => handleDelete(record.id)}
            okText="确定"
            cancelText="取消"
          >
            <Button type="link" danger icon={<DeleteOutlined />}>
              删除
            </Button>
          </Popconfirm>
        </Space>
      )
    }
  ]
  
  return (
    <div>
      <div className="search-bar" style={{ marginBottom: 16, padding: 16, background: '#fff', borderRadius: 8 }}>
        <Space size="large">
          <div>
            <Input
              placeholder="搜索工号/姓名/手机号"
              prefix={<SearchOutlined />}
              value={filters.keyword}
              onChange={e => setFilters({ ...filters, keyword: e.target.value })}
              onPressEnter={handleSearch}
              style={{ width: 300 }}
            />
          </div>
          <div>
            <Select
              placeholder="选择部门"
              value={filters.departmentId}
              onChange={value => setFilters({ ...filters, departmentId: value })}
              style={{ width: 150 }}
              allowClear
            >
              {/* 部门选项将通过API加载 */}
            </Select>
          </div>
          <div>
            <Select
              placeholder="选择岗位"
              value={filters.positionId}
              onChange={value => setFilters({ ...filters, positionId: value })}
              style={{ width: 150 }}
              allowClear
            >
              {/* 岗位选项将通过API加载 */}
            </Select>
          </div>
          <div>
            <Select
              placeholder="选择状态"
              value={filters.status}
              onChange={value => setFilters({ ...filters, status: value })}
              style={{ width: 150 }}
              allowClear
            >
              <Option value="在职">在职</Option>
              <Option value="离职">离职</Option>
              <Option value="试用">试用</Option>
            </Select>
          </div>
          <div>
            <RangePicker
              placeholder={['入职日期开始', '入职日期结束']}
              value={filters.entryDateRange}
              onChange={value => setFilters({ ...filters, entryDateRange: value })}
              style={{ width: 300 }}
            />
          </div>
          <Button type="primary" onClick={handleSearch}>
            搜索
          </Button>
          <Button onClick={handleReset}>
            重置
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增员工
          </Button>
        </Space>
      </div>
      
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{
          ...pagination,
          total,
          onChange: handlePageChange
        }}
      />
    </div>
  )
}

export default EmployeeList
```

## 4️⃣ GMP合规特性实现

### 4.1 资质证书有效期管理

系统实现了完善的资质证书有效期管理功能，包括证书到期提醒、自动权限调整等。

**证书有效期检查调度任务**：

```java
@Component
@Slf4j
public class CertificateExpiryChecker {
    @Autowired
    private QualificationCertificateRepository certificateRepository;
    
    @Autowired
    private DynamicPermissionService dynamicPermissionService;
    
    @Autowired
    private NotificationService notificationService;
    
    // 每日凌晨1点执行证书有效期检查
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkCertificateExpiry() {
        log.info("开始检查证书有效期");
        
        // 获取即将过期的证书（30天内）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date thirtyDaysLater = calendar.getTime();
        
        List<QualificationCertificate> expiringCertificates = certificateRepository
                .findByStatusAndExpiryDateBetween("有效", new Date(), thirtyDaysLater);
        
        // 处理即将过期的证书
        for (QualificationCertificate certificate : expiringCertificates) {
            // 计算剩余天数
            long daysRemaining = (certificate.getExpiryDate().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
            
            // 发送提醒通知
            String message = String.format(
                "员工%s的证书《%s》（编号：%s）将在%d天后过期，请及时办理复审或更新。",
                certificate.getEmployee().getName(),
                certificate.getCertificateName(),
                certificate.getCertificateNo(),
                daysRemaining
            );
            
            // 通知员工本人和相关管理员
            notificationService.sendNotification(
                certificate.getEmployeeId(),
                "证书即将过期提醒",
                message,
                NotificationType.EMAIL
            );
            
            // 通知HR管理员
            notificationService.sendToRole(
                "HR_ADMIN",
                "证书即将过期提醒",
                message,
                NotificationType.SYSTEM
            );
            
            log.info("已通知证书即将过期: {}", certificate.getId());
        }
        
        // 检查已过期的证书
        List<QualificationCertificate> expiredCertificates = certificateRepository
                .findByStatusAndExpiryDateBefore("有效", new Date());
        
        // 处理已过期的证书
        for (QualificationCertificate certificate : expiredCertificates) {
            // 更新证书状态
            certificate.setStatus("过期");
            certificateRepository.updateById(certificate);
            
            // 调整员工权限
            dynamicPermissionService.adjustPermissionsByQualification(certificate.getEmployeeId());
            
            // 发送过期通知
            String message = String.format(
                "员工%s的证书《%s》（编号：%s）已过期，相关GMP操作权限已自动调整。",
                certificate.getEmployee().getName(),
                certificate.getCertificateName(),
                certificate.getCertificateNo()
            );
            
            notificationService.sendNotification(
                certificate.getEmployeeId(),
                "证书已过期通知",
                message,
                NotificationType.EMAIL
            );
            
            notificationService.sendToRole(
                "HR_ADMIN",
                "证书已过期通知",
                message,
                NotificationType.SYSTEM
            );
            
            log.info("已处理过期证书: {}, 员工权限已调整", certificate.getId());
        }
        
        log.info("证书有效期检查完成，发现{}个即将过期证书，{}个已过期证书", 
            expiringCertificates.size(), expiredCertificates.size());
    }
}
```

### 4.2 GMP活动参与追溯

系统实现了GMP活动参与追溯功能，将员工的考勤记录与GMP生产活动关联，确保生产活动的人员参与可追溯。

**GMP活动关联实现**：

```java
@Service
@Slf4j
public class GmpActivityAttendanceServiceImpl implements GmpActivityAttendanceService {
    @Autowired
    private GmpActivityAttendanceRepository gmpActivityAttendanceRepository;
    
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    
    @Autowired
    private GmpActivityRepository gmpActivityRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateAttendanceWithGmpActivity(Long attendanceId, Long activityId) {
        // 验证考勤记录和GMP活动是否存在
        AttendanceRecord attendance = attendanceRecordRepository.getById(attendanceId);
        if (attendance == null) {
            throw new BusinessException("考勤记录不存在");
        }
        
        GmpActivity activity = gmpActivityRepository.getById(activityId);
        if (activity == null) {
            throw new BusinessException("GMP活动不存在");
        }
        
        // 验证考勤时间与活动时间是否匹配
        Date attendanceDate = attendance.getCheckDate();
        Date activityDate = activity.getStartTime();
        
        Calendar attendanceCal = Calendar.getInstance();
        attendanceCal.setTime(attendanceDate);
        
        Calendar activityCal = Calendar.getInstance();
        activityCal.setTime(activityDate);
        
        boolean sameDate = attendanceCal.get(Calendar.YEAR) == activityCal.get(Calendar.YEAR) &&
                          attendanceCal.get(Calendar.MONTH) == activityCal.get(Calendar.MONTH) &&
                          attendanceCal.get(Calendar.DAY_OF_MONTH) == activityCal.get(Calendar.DAY_OF_MONTH);
        
        if (!sameDate) {
            throw new BusinessException("考勤日期与GMP活动日期不匹配");
        }
        
        // 更新考勤记录的GMP活动ID
        attendance.setGmpActivityId(activityId);
        attendanceRecordRepository.updateById(attendance);
        
        // 创建GMP活动参与记录
        GmpActivityAttendance gmpAttendance = new GmpActivityAttendance();
        gmpAttendance.setActivityId(activityId);
        gmpAttendance.setEmployeeId(attendance.getEmployeeId());
        gmpAttendance.setAttendanceId(attendanceId);
        gmpAttendance.setParticipationTime(attendance.getCheckInTime());
        gmpAttendance.setRole("参与人员");
        
        gmpActivityAttendanceRepository.save(gmpAttendance);
        
        log.info("已关联考勤记录{}与GMP活动{}", attendanceId, activityId);
    }
    
    @Override
    public List<Employee> getActivityParticipants(Long activityId) {
        // 获取活动参与人员的考勤记录ID
        List<Long> attendanceIds = gmpActivityAttendanceRepository.findByActivityId(activityId)
                .stream().map(GmpActivityAttendance::getAttendanceId).collect(Collectors.toList());
        
        if (attendanceIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取对应的员工ID
        List<Long> employeeIds = attendanceRecordRepository.findByIds(attendanceIds)
                .stream().map(AttendanceRecord::getEmployeeId).collect(Collectors.toList());
        
        if (employeeIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取员工信息
        return employeeRepository.listByIds(employeeIds);
    }
    
    @Override
    public List<GmpActivity> getEmployeeActivities(Long employeeId, Date startDate, Date endDate) {
        // 获取员工在指定时间范围内的考勤记录
        List<AttendanceRecord> attendances = attendanceRecordRepository
                .findByEmployeeIdAndCheckDateBetween(employeeId, startDate, endDate);
        
        if (attendances.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 提取有GMP活动关联的记录
        List<Long> activityIds = attendances.stream()
                .filter(a -> a.getGmpActivityId() != null)
                .map(AttendanceRecord::getGmpActivityId)
                .collect(Collectors.toList());
        
        if (activityIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取GMP活动信息
        return gmpActivityRepository.listByIds(activityIds);
    }
}
```

## 5️⃣ 系统集成实现

### 5.1 与认证系统集成

系统与企业认证系统集成，实现统一的身份认证和授权。

**OAuth2集成配置**：

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/api/v1/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("gmp-hr-system");
    }
}

@Configuration
public class JwtTokenStoreConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSecret);
        return converter;
    }
}
```

### 5.2 与GMP系统集成

系统与企业GMP系统集成，实现数据共享和业务协同。

**GMP系统API集成服务**：

```java
@Service
@Slf4j
public class GmpSystemIntegrationService {
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${gmp.system.api.url}")
    private String gmpApiUrl;
    
    @Value("${gmp.system.api.key}")
    private String gmpApiKey;
    
    // 同步员工信息到GMP系统
    public boolean syncEmployeeToGmpSystem(Employee employee) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", gmpApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 转换员工信息为GMP系统需要的格式
            GmpEmployeeDTO gmpEmployee = new GmpEmployeeDTO();
            gmpEmployee.setEmployeeId(employee.getId());
            gmpEmployee.setEmployeeCode(employee.getEmployeeCode());
            gmpEmployee.setName(employee.getName());
            gmpEmployee.setDepartmentName(employee.getDepartmentName());
            gmpEmployee.setPositionName(employee.getPositionName());
            
            // 发送请求
            HttpEntity<GmpEmployeeDTO> request = new HttpEntity<>(gmpEmployee, headers);
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                gmpApiUrl + "/api/employees", request, ApiResponse.class);
            
            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            log.error("同步员工信息到GMP系统失败: {}", e.getMessage());
            return false;
        }
    }
    
    // 从GMP系统获取GMP活动信息
    public List<GmpActivityDTO> getGmpActivities(Date startDate, Date endDate) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", gmpApiKey);
            
            // 构建请求参数
            HttpEntity<String> request = new HttpEntity<>(headers);
            Map<String, String> params = new HashMap<>();
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(startDate));
            params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(endDate));
            
            // 发送请求
            ResponseEntity<ApiResponse<List<GmpActivityDTO>>> response = restTemplate.exchange(
                gmpApiUrl + "/api/activities", HttpMethod.GET, request, 
                new ParameterizedTypeReference<ApiResponse<List<GmpActivityDTO>>>() {}, params);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess()) {
                return response.getBody().getData();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("从GMP系统获取活动信息失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // 验证员工是否具备GMP操作资质
    public boolean validateEmployeeGmpQualification(Long employeeId, String operationType) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", gmpApiKey);
            
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("employeeId", employeeId.toString());
            params.put("operationType", operationType);
            
            // 发送请求
            ResponseEntity<ApiResponse<Boolean>> response = restTemplate.getForEntity(
                gmpApiUrl + "/api/qualifications/validate?employeeId={employeeId}&operationType={operationType}", 
                new ParameterizedTypeReference<ApiResponse<Boolean>>() {}, params);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess()) {
                return response.getBody().getData();
            }
            return false;
        } catch (Exception e) {
            log.error("验证员工GMP操作资质失败: {}", e.getMessage());
            return false;
        }
    }
}
```

## 6️⃣ 本章小结

本章详细介绍了GMP环境下人事管理子系统的实现过程，包括技术架构实现、核心功能模块实现、数据库实现、安全实现和GMP合规特性实现等内容。

系统采用微服务架构，基于Spring Boot + Spring Cloud构建了完善的后端服务体系，实现了服务注册与发现、API网关、配置中心等核心功能。前端基于React + Ant Design实现了用户友好的界面，支持响应式设计和组件化开发。

在核心功能模块实现方面，详细介绍了组织架构管理、员工信息管理、GMP合规性管理等模块的实现细节，包括实体类设计、服务层实现、控制器实现和前端组件实现等。特别是GMP合规特性的实现，如动态权限控制、资质证书有效期管理、GMP活动参与追溯等，确保了系统的GMP合规性。

系统还实现了与认证系统和GMP系统的集成，支持统一身份认证和数据共享，提高了系统的集成性和扩展性。

通过本系统的实现，我们成功构建了一个符合GMP要求的人事管理子系统，满足了制药企业对GMP环境下人事管理的特殊需求，提高了人事管理的效率和合规性。

---

*文档版本：v0.1.0-draft*
*审核状态：待审核*
*下次更新：根据实现调整*