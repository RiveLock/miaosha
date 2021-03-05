package com.general.miaosha.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.general.miaosha.common.pojo.BaseEntity;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 代码生成器
 */
public class CodeGenerator {

    /**
     * 是否运行初始化脚本
     */
    private static final boolean isRunScript = false;

    /**
     * 初始化表数据位置
     */
    private static final String TABLE_DATA = "/db/table-data.sql";

    /**
     * 包名，最终生成路径
     */
    private static final String PAKEGE_NAME = "com.general.miaosha.business";

    /**
     * 数据库链接
     */
    private static final String JDBC = "jdbc:mysql://localhost:3306/miaosha?characterEncoding=UTF8";

    /**
     * 数据库账户
     */
    private static final String USER_NAME = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "root";

    /**
     * 代码作者
     */
    private static final String AUTHOR = "John J";

    /**
     * 生成代码
     */
    public static void main(String[] args) throws SQLException {
        new AutoGenerator(mysqlDataSourceConfig()).global(globalConfig())  // 全局
                .packageInfo(packageConfig())  // 包名
                .strategy(strategyConfig())  // 策略
                .engine(new FreemarkerTemplateEngine())  // 引擎
                .template(templateConfig())  // 模版
                .injection(injectionConfig()).execute();
    }

    /**
     * 运行脚本
     */
    private static void runScript(DataSourceConfig dataSourceConfig) throws SQLException {
        try (Connection connection = dataSourceConfig.getConn()) {
            InputStream inputStream = CodeGenerator.class.getResourceAsStream(TABLE_DATA);
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setAutoCommit(true);
            scriptRunner.runScript(new InputStreamReader(inputStream));
        }
    }

    /**
     * Mysql数据源配置
     */
    private static DataSourceConfig mysqlDataSourceConfig() throws SQLException {
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(JDBC, USER_NAME, PASSWORD).driver(com.mysql.cj.jdbc.Driver.class)
                .build();
        // 运行脚本，添加表和数据
        if (isRunScript) {
            runScript(dataSourceConfig);
        }
        return dataSourceConfig;
    }

    /**
     * 全局配置
     */
    private static GlobalConfig globalConfig() {
        // fileOverride 是否覆盖文件
        final String outPutDir = System.getProperty("user.dir") + "/src/main/java";
        return new GlobalConfig.Builder().serviceName("%sService").outputDir(outPutDir).author(AUTHOR).openDir(true).fileOverride(false)
                .swagger2(true).build();
    }

    /**
     * 包配置
     */
    private static PackageConfig packageConfig() {
        return new PackageConfig.Builder().parent(PAKEGE_NAME).moduleName(scanner("模块名")).build();
    }

    /**
     * 策略配置
     */
    private static StrategyConfig strategyConfig() {
        return new StrategyConfig.Builder().addInclude(scanner("表名")).addTablePrefix("t_") //统一表前缀
                .entityBuilder()  // 实体相关配置
                .naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel).lombok(true)
                .addSuperEntityColumns("id", "create_time", "update_time").superClass(BaseEntity.class)   //自动识别父类字段
                .addTableFills(new Column("create_time", FieldFill.INSERT))     //基于字段填充
                .addTableFills(new Property("updateTime", FieldFill.UPDATE))    //基于属性填充
                .controllerBuilder().hyphenStyle(true)  //控制器相关配置
                .build();
    }

    /**
     * 控制台输入
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 自定义模板配置
     */
    private static TemplateConfig templateConfig() {
        return new TemplateConfig.Builder().all()   //激活所有默认模板
                .build().disable(TemplateType.XML);
    }

    /**
     * 自定义配置
     */
    private static InjectionConfig injectionConfig() {
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig();
        // 模板引擎 freemarker
        String mapperTemplatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(mapperTemplatePath) {
            @Override
            public File outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return new File(System.getProperty("user.dir") + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
                        + StringPool.DOT_XML);
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

}
