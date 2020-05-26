# dplPlus
Berkeley DB java api 有3种使用方式：1、DPL 2、基础API 3、Collections框架。以上三种方式，在使用过程中都比较麻烦，
而且要写很多重复代码。其中DPL方式相对使用简单，但还是不够理想。为了简化应用，作者开发了dplPlus插件。该插件提供了数据
库常用的方法，只需要简单的配置就可以使用。API使用比较方便，大多数API只需要一行代码即可。以上所述都是单表（实体类）操
作，如果需要操作多表或者其他复杂的事务，可以通过execute方法，传入一个lambda表达式即可。该表达式提供了store、txn两
个参数，使用者可以通过store来获取主键、二级索引，从而使用原生方法，如果需要使用事务，则使用原生的事务方法，并传入txn
参数即可。

## 使用方法

##### 1、引入jar包
     <dependencies>
         <!-- Berkeley DB Java Edition -->
         <dependency>
             <groupId>com.sleepycat</groupId>
             <artifactId>je</artifactId>
             <version>6.4.9</version>
         </dependency>
         <!-- dplPlus -->
         <dependency>
             <groupId>org.garen.plus</groupId>
             <artifactId>dplPlus</artifactId>
             <version>1.0</version>
             <exclusions>
                <!-- 排除插件中引用的 je jar包 -->
                 <exclusion>
                     <groupId>com.sleepycat</groupId>
                     <artifactId>je</artifactId>
                 </exclusion>
                 <!-- 排除插件中引用的 jespring-beans jar包 -->
                 <exclusion>
                     <groupId>org.springframework</groupId>
                     <artifactId>spring-beans</artifactId>
                 </exclusion>
             </exclusions>
         </dependency>
     </dependencies>
     <!-- Berkeley DB 的源地址 -->
     <repositories>
         <repository>
             <id>oracleReleases</id>
             <name>Oracle Released Java Packages</name>
             <url>http://download.oracle.com/maven</url>
             <layout>default</layout>
         </repository>
     </repositories>
     
##### 2、配置参数（包括但不限于 springboot 方式，例子使用 springboot 方式）
    # BerkeleyDB 参数配置
    BerkeleyDB:
      # BDB 环境路径
      envPath: /Users/liuxueliang/Desktop/BDB
      # BDB 仓库名称
      storeName: myStore
      
##### 3、传入参数（包括但不限于 springboot 方式，例子使用 springboot 方式）
    import org.springframework.boot.ApplicationArguments;
    import org.springframework.boot.ApplicationRunner;
    import org.springframework.stereotype.Component;
    
    /**
     * <p>
     * 功能描述 : 项目启动成功后执行
     * </p>
     *
     * @author : Garen Gosling 2020/4/3 下午2:58
     */
    @Slf4j
    @Component
    public class MyApplicationRunner implements ApplicationRunner {
    
        @Value("${BerkeleyDB.envPath}")
        private String ENV_PATH;
    
        @Value("${BerkeleyDB.storeName}")
        private String STORE_NAME;
    
        @Override
        public void run(ApplicationArguments args) throws Exception {
            // 核心!!!不管用什么形式，只要保证 dplPlus api 使用前执行这句就行
            DplConfig.getInstance().init(ENV_PATH, STORE_NAME); 
        }
    }
    
##### 4、实体类（与数据库表映射，必需用@Entity修饰类，必需有主键且用@PrimaryKey修饰主键）
    import com.sleepycat.persist.model.Entity;
    import com.sleepycat.persist.model.PrimaryKey;
    import com.sleepycat.persist.model.SecondaryKey;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import static com.sleepycat.persist.model.Relationship.ONE_TO_ONE;
    
    /**
     * <p>
     * 功能描述 : AI 应用
     * </p>
     *
     * @author : Garen Gosling 2020/5/8 上午11:43
     */
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Entity
    public class AiApp {
        /**
         * ID
         */
        @PrimaryKey
        private String id;
    
        /**
         * 名称
         * 注意：如果需要用条件查询，需要标记为二级索引
         */
        @SecondaryKey(relate=ONE_TO_ONE)
        private String name;
        
        ...
        
##### 5、业务层接口（需要继承 dplPlus 的父接口 IDplService<PK, E>）
    import ogd.berkeleyDB.easyDPL.entity.AiApp;
    import org.garen.plus.dplPlus.IDplService;
    
    /**
     * <p>
     * 功能描述 : 业务接口 - AI 应用
     * </p>
     *
     * @author : Garen Gosling 2020/5/23 下午3:05
     */
    public interface IAiAppService extends IDplService<String, AiApp> {
    
    }
    
##### 6、业务层实现类（需要继承 dplPlus 的基类 DplServiceImpl<PK, E>）
    import ogd.berkeleyDB.easyDPL.entity.AiApp;
    import ogd.berkeleyDB.easyDPL.service.IAiAppService;
    import org.garen.plus.dplPlus.DplServiceImpl;
    import org.springframework.stereotype.Component;
    
    /**
     * <p>
     * 功能描述 : 映射类 - AI 应用
     * </p>
     *
     * @author : Garen Gosling 2020/5/23 下午12:05
     */
    @Component
    public class AiAppServiceImpl extends DplServiceImpl<String, AiApp> implements IAiAppService {
    
    }
    
## 使用示例
    import ogd.berkeleyDB.easyDPL.entity.AiApp;
    import ogd.berkeleyDB.easyDPL.service.IAiAppService;
    import ogd.berkeleyDB.response.DataResult;
    import ogd.berkeleyDB.response.ResultCodeEnum;
    import ogd.berkeleyDB.response.ResultEnum;
    import org.garen.plus.dplPlus.Page;
    import org.garen.plus.dplPlus.PkUtils;
    import org.springframework.web.bind.annotation.*;
    
    import javax.annotation.Resource;
    import java.util.List;
    
    @RestController
    @RequestMapping(value = "/aiApp")
    public class AiAppController {
    
        @Resource
        IAiAppService aiAppService;
    
        @RequestMapping(value = "/save", method = RequestMethod.POST)
        public DataResult save(@RequestBody AiApp aiApp) {
            aiApp.setId(PkUtils.uuid());    // 设置主键
            AiApp result = aiAppService.save(aiApp.getId(), aiApp);
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    
        @RequestMapping(value = "/get", method = RequestMethod.GET)
        public DataResult get(@RequestParam String id) {
            AiApp result = aiAppService.get(id);
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    
        @RequestMapping(value = "/update", method = RequestMethod.POST)
        public DataResult update(@RequestBody AiApp aiApp) {
            AiApp result = aiAppService.update(aiApp.getId(), aiApp);
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        public DataResult delete(@RequestParam String id) {
            aiAppService.delete(id);
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, null);
        }
    
        @RequestMapping(value = "/listAll", method = RequestMethod.GET)
        public DataResult listAll() {
            List<AiApp> result = aiAppService.listAll();
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    
        @RequestMapping(value = "/listByName", method = RequestMethod.GET)
        public DataResult listByName(@RequestParam String name) {
            List<AiApp> result = aiAppService.listBySk("name", String.class, name);
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    
        @RequestMapping(value = "/pageAll", method = RequestMethod.GET)
        public DataResult pageAll(@RequestParam(required = false, value = "current") Integer current,
                               @RequestParam(required = false, value = "size") Integer size) {
            Page<AiApp> result = aiAppService.pageAll(current, size, aiAppService.listAll());
            return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
        }
    }
    
## 单表多条件查询示例

##### Controller    
    @RequestMapping(value = "/getByParams", method = RequestMethod.GET)
    public DataResult getByParams(@RequestParam(required = false, value = "name") String name,
                                  @RequestParam(required = false, value = "type") Integer type) {
        List<Engine> result = engineService.getByParams(name, type);
        return new DataResult<>(true, ResultEnum.RESULT_SUCCESS, ResultCodeEnum.RESULT_SUCCESS, result);
    }
##### Service  
    import ogd.berkeleyDB.easyDPL.entity.Engine;
    import org.garen.plus.dplPlus.IDplService;
    
    import java.util.List;
    
    /**
     * <p>
     * 功能描述 : 业务接口 - 领域
     * </p>
     *
     * @author : Garen Gosling 2020/5/23 下午3:05
     */
    public interface IEngineService extends IDplService<String, Engine> {
        /**
         * <p>
         * 功能描述 : 条件查询
         * </p>
         *
         * @author : Garen Gosling   2020/5/25 下午6:36
         *
         * @param name 应用名称
         * @param type 类型
         * @Return java.util.List<ogd.berkeleyDB.easyDPL.entity.Engine>
         **/
        List<Engine> getByParams(String name, Integer type);
    }
##### ServiceImpl
    import com.sleepycat.persist.SecondaryIndex;
    import ogd.berkeleyDB.easyDPL.entity.Engine;
    import ogd.berkeleyDB.easyDPL.service.IEngineService;
    import org.garen.plus.dplPlus.DplServiceImpl;
    import org.garen.plus.dplPlus.Param;
    import org.springframework.stereotype.Component;
    import org.springframework.util.StringUtils;
    
    import java.util.List;
    
    /**
     * <p>
     * 功能描述 : 映射类 - 领域
     * </p>
     *
     * @author : Garen Gosling 2020/5/23 下午12:05
     */
    @Component
    public class EngineServiceImpl extends DplServiceImpl<String, Engine> implements IEngineService {
    
        @Override
        public List<Engine> getByParams(String name, Integer type) {
            return listByParams(((store, pi) -> {
                // 声明参数
                Param<String, String, Engine> param1 = null;
                Param<Integer, String, Engine> param2 = null;
                // 参数 1
                if(!StringUtils.isEmpty(name)){
                    SecondaryIndex<String, String, Engine> si_name = store.getSecondaryIndex(pi, String.class, "name");
                    param1 = new Param<>(si_name, name);
                }
                // 参数 2
                if(!StringUtils.isEmpty(type)){
                    SecondaryIndex<Integer, String, Engine> si_type = store.getSecondaryIndex(pi, Integer.class, "type");
                    param2 = new Param<>(si_type, type);
                }
                // 返回参数集合
                return toParamList(param1, param2);
            }));
        }
    }


## 多表事务操作示例（不需要使用事务则传txn的地方传null即可）

##### Service  
    /**
     * <p>
     * 功能描述 : 用 dplPlus 的 execute ，使用原生方法
     * </p>
     *
     * @author : Garen Gosling   2020/5/26 下午5:22
     *
     * @param
     * @Return void
     **/
    String useExecute();
    
##### ServiceImpl
    @Override
    public String useExecute() {
        return execute(((store, txn) -> {
            // aiApp 对象主键、二级索引
            PrimaryIndex<String, AiApp> aiAppPI = store.getPrimaryIndex(String.class, AiApp.class);
            SecondaryIndex<String, String, AiApp> aiAppSiName = store.getSecondaryIndex(aiAppPI, String.class, "name");

            // engine 对象主键、二级索引
            PrimaryIndex<String, Engine> enginePI = store.getPrimaryIndex(String.class, Engine.class);
            SecondaryIndex<String, String, Engine> engineSiName = store.getSecondaryIndex(enginePI, String.class, "name");
            SecondaryIndex<Integer, String, Engine> engineSiType = store.getSecondaryIndex(enginePI, Integer.class, "type");

            // 复杂操作，随便写几个意思一下
            AiApp aiApp = aiAppPI.get(txn, "82752fad926a44a1af652245dc83a625", LockMode.DEFAULT);
            aiApp.setDescription("hello world");
            aiAppPI.put(txn, aiApp);
            aiAppSiName.get("hello");
            enginePI.delete(txn, "99992fad926a44a1af652245dc839999");
            engineSiName.get(txn, "engine-1", LockMode.DEFAULT);
            engineSiType.get(txn, 0, LockMode.DEFAULT);
            return aiApp.getName();
        }));
    }
    