package com.wendy.demo.service.impl;

import com.wendy.demo.entity.*;
import com.wendy.demo.service.GeneratorService;
import com.wendy.demo.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/10/15 17:46
 * @Version 1.0
 */
@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {
    private static final String ROOT_PATH = "D:/local_file_server/spring_demo_generator/";

    @Override
    public byte[] generateProject(ProjectReq projectReq) {
        ProjectInfo projectInfo = createProject(projectReq);

        FilePathInfo filePathInfo = projectInfo.getFilePathInfo();
        writePomContent(projectReq, filePathInfo.getPomFilePath());
        writeApplicationContent(projectReq, projectInfo);

        compressProject(ROOT_PATH + projectReq.getName());
        return new byte[0];
    }

    private void compressProject(String directory) {
        try {
            File file = new File(directory);
            File zipFile = new File("D:/local_file_server/spring_demo_generator/router.zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            compress(file, zos, file.getName());
            zos.close();
        } catch (IOException e) {
            log.error("Compress directory to zip error.", e);
            throw new RuntimeException("Compress directory to zip error.");
        }
    }

    public static void compress(File sourceFile, ZipOutputStream zos, String name) throws IOException {
        byte[] buf = new byte[1024];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 空文件夹的处理
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (File file : listFiles) {
                    // file.getName()前面需要带上父文件夹的名字加一斜杠,
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
    }

    private ProjectInfo createProject(ProjectReq projectReq) {
        try {
            FilePathInfo filePathInfo = new FilePathInfo();
            ApplicationInfo applicationInfo = new ApplicationInfo();

            String servicePath = ROOT_PATH + projectReq.getName() + "/src/main/java/"
                    + projectReq.getGroupId().replace(".", "/");
            File file = new File(servicePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            String applicationName = getApplicationName(projectReq);
            String applicationPath = servicePath + "/" + applicationName + ".java";
            filePathInfo.setApplicationFilePath(applicationPath);
            File applicationFile = new File(applicationPath);
            if (!applicationFile.exists()) {
                applicationFile.createNewFile();
            }
            applicationInfo.setApplicationName(applicationName);
            applicationInfo.setPackagePath(projectReq.getGroupId());

            String staticPath = ROOT_PATH + projectReq.getName() + "/src/main/resources/static";
            File staticDirectory = new File(staticPath);
            if (!staticDirectory.exists()) {
                staticDirectory.mkdirs();
            }

            String templatesPath = ROOT_PATH + projectReq.getName() + "/src/main/resources/templates";
            File templatesDirectory = new File(templatesPath);
            if (!templatesDirectory.exists()) {
                templatesDirectory.mkdirs();
            }

            String applicationYmlPath = ROOT_PATH + projectReq.getName() + "/src/main/resources/application.yml";
            File applicationYmlFile = new File(applicationYmlPath);
            if (!applicationYmlFile.exists()) {
                applicationYmlFile.createNewFile();
            }

            String testPath = ROOT_PATH + projectReq.getName() + "/src/test";
            File testDirectory = new File(testPath);
            if (!testDirectory.exists()) {
                testDirectory.mkdirs();
            }

            String readmePath = ROOT_PATH + projectReq.getName() + "/README.md";
            File readmeFile = new File(readmePath);
            if (!readmeFile.exists()) {
                readmeFile.createNewFile();
            }

            String pomPath = ROOT_PATH + projectReq.getName() + "/pom.xml";
            filePathInfo.setPomFilePath(pomPath);
            File pomFile = new File(pomPath);
            if (!pomFile.exists()) {
                pomFile.createNewFile();
            }

            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setFilePathInfo(filePathInfo);
            projectInfo.setApplicationInfo(applicationInfo);
            return projectInfo;
        } catch (IOException e) {
            log.error("Create project error, the reason is {}", e.getMessage());
            throw new RuntimeException("Create project error");
        }

    }

    private String getApplicationName(ProjectReq projectReq) {
        String name = projectReq.getName();
        StringBuilder sb = new StringBuilder();
        sb.append((name.charAt(0) + "").toUpperCase());
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isLetter(c)) {
                if (name.charAt(i - 1) == '-' || name.charAt(i - 1) == '_') {
                    sb.append((c + "").toUpperCase());
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString() + "Application";
    }

    private void writeApplicationContent(ProjectReq projectReq, ProjectInfo projectInfo) {
        ClassPathResource resource = new ClassPathResource("templates/ApplicationTemplate");
        try (InputStream inputStream = resource.getInputStream()) {
            // 读取模板内容
            StringBuilder sb = new StringBuilder();
            byte[] data = new byte[1024];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                // 只将有内容的字节流转为字符串，拼接在文件内容后面
                sb.append(new String(data, 0, len, StandardCharsets.UTF_8));
            }

            // 生成Springboot启动类内容
            String content = sb.toString();
            content = content.replace("PACKAGE_PATH", projectReq.getGroupId());
            content = content.replace("APPLICATION_NAME", projectInfo.getApplicationInfo().getApplicationName());

            // 写入Springboot启动类
            FileWriter writer = new FileWriter(projectInfo.getFilePathInfo().getApplicationFilePath());
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            log.error("Read ApplicationTemplate error.");
            throw new RuntimeException("Read ApplicationTemplate error.");
        }
    }

    private void writePomContent(ProjectReq projectReq, String pomFilePath) {
        Document document = createPomDocument(projectReq);
        generatePomXml(document, pomFilePath);
    }

    private Document createPomDocument(ProjectReq projectReq) {
        // 创建一个Document实例，代表一个xml文件
        Document document = DocumentHelper.createDocument();

        // 设置maven的根属性
        Element project = document.addElement("project");
        project.addAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
        project.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        project.addAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd");

        Element modelVersion = project.addElement("modelVersion");
        modelVersion.addText("4.0.0");

        // 设置父项目的信息
        Element parent = project.addElement("parent");
        Dependency parentInfo = projectReq.getParent();
        if (Objects.isNull(parentInfo)) {
            parent.addElement("groupId").addText("org.springframework.boot");
            parent.addElement("artifactId").addText("spring-boot-starter-parent");
            parent.addElement("version").addText("2.7.15");
            parent.addElement("relativePath");
        } else {
            parent.addElement("groupId").addText(parentInfo.getGroupId());
            parent.addElement("artifactId").addText(parentInfo.getArtifactId());
            parent.addElement("version").addText(parentInfo.getVersion());
            parent.addElement("relativePath");
        }

        // 设置项目的基本信息
        project.addElement("groupId").addText(projectReq.getGroupId());
        project.addElement("artifactId").addText(projectReq.getArtifactId());
        project.addElement("version").addText("1.0.0-SNAPSHOT");
        project.addElement("name").addText(projectReq.getName());

        if (StringUtils.isEmpty(projectReq.getDescription())) {
            project.addElement("description").addText("default description.");
        } else {
            project.addElement("description").addText(projectReq.getDescription());
        }

        // 设置依赖的版本信息
        Element properties = project.addElement("properties");
        properties.addElement("java.version").addText("11");

        // 设置maven的依赖软件
        Element dependencies = project.addElement("dependencies");
        try {
            for (Dependency dependencyInfo : projectReq.getDependencies()) {
                Element dependency = dependencies.addElement("dependency");

                // 利用反射设置dependendy的子元素
                Class clazz = dependencyInfo.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldValue = (String) field.get(dependencyInfo);
                    if (StringUtils.isNotEmpty(fieldValue)) {
                        dependency.addElement(field.getName()).addText(fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Set project dependencies error.", e);
        }

        // 设置maven的构建插件
        Element build = project.addElement("build");
        Element plugins = build.addElement("plugins");
        Element plugin = plugins.addElement("plugin");
        plugin.addElement("groupId").addText("org.springframework.boot");
        plugin.addElement("artifactId").addText("spring-boot-maven-plugin");

        return document;
    }

    private void generatePomXml(Document document, String pomFilePath) {
        File pomXml = new File(pomFilePath);
        FileUtils.createFile(pomXml);

        // 自定义xml样式
        OutputFormat format = new OutputFormat();
        format.setIndentSize(4);  // 行缩进
        format.setNewlines(true); // 一个结点为一行
        format.setTrimText(true); // 去重空格
        format.setPadText(true);
        format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行

        try {
            OutputStream outputStream = new FileOutputStream(pomXml);
            XMLWriter xmlWriter = new XMLWriter(outputStream, format);
            xmlWriter.write(document);
        } catch (Exception e) {
            throw new RuntimeException("Create maven dependency error.");
        }
    }
}
