import nl.javadude.scannit.Configuration;
import nl.javadude.scannit.Scannit;
import nl.javadude.scannit.scanner.SubTypeScanner;
import nl.javadude.scannit.scanner.TypeAnnotationScanner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by İlker ÇATAK on 31.05.2018.
 */
public class ScannerMain {

    private static final Logger LOG = LoggerFactory.getLogger(ScannerMain.class);
    private static final String BASE_PACKAGE_NAME="com.ispark.otopark.";
    private static final String SOURCE_PROJECT_FOLDER_BASE_LOCATION ="/home/ilker/Desktop/generate/generate4/src/";
    private static final String MAIN_PROJECT="main/java/com/ispark/otopark/";
    private static final String TEST_PROJECT="test/java/com/ispark/otopark/";

    private static final String DOMAIN_PACKAGE="domain";
    private static final String RESOURCE_PACKAGE="web/rest";
    private static final String SERVICE_PACKAGE="service";
    private static final String REPOSITORY_PACKAGE="repository";
    private static final String TEST_PACKAGE="test";
    private static final String DTO_PACKAGE = "service/dto";
    private static final String MAPPER_PACKAGE = "service/mapper";

    private static final String PROJECT_FOLDER_NAME="/IdeaProjects";
    final static  String userHome= System.getProperty("user.home");
    private static final String DESTINATION_ROOT_FOLDER_NAME="CopyLocations";
    private static final String ROOT_FILE_PATH=userHome+ PROJECT_FOLDER_NAME+"/"+DESTINATION_ROOT_FOLDER_NAME;
    private static final String SLASH="/";

    public static void main(String[] args) {
        ScannerMain app = new ScannerMain();
        app.run();
    }

    public void run() {
        if(!createNewFolder(ROOT_FILE_PATH)){
            LOG.error("Could not create directory in :"+ ROOT_FILE_PATH);
            return;
        }
        Configuration config= Configuration.config().with(new SubTypeScanner(),new TypeAnnotationScanner()).scan(BASE_PACKAGE_NAME+DOMAIN_PACKAGE);
        Scannit scannit= new Scannit(config);
        Set<Class<?>> entityClasses = scannit.getTypesAnnotatedWith(Entity.class);
        for (Class<?> clazz: entityClasses) {
            copyFilesToNewLocation(clazz.getSimpleName());
        }
    }


    /**
     * Domain , Resource , Repository, Service için ayrı ayrı dosyaları kopyalar ve tek bir location'a atar.
     * @param clazzSimpleName
     */
    private void copyFilesToNewLocation(String clazzSimpleName) {
        createNewFolder(ROOT_FILE_PATH+SLASH+clazzSimpleName);
        copyDomainFile(clazzSimpleName);
        copyResourceFile(clazzSimpleName);
        copyRepositoryFile(clazzSimpleName);
        copyServiceFile(clazzSimpleName);
        copyTestFile(clazzSimpleName);
        copyDTOFile(clazzSimpleName);
        copyMapperFile(clazzSimpleName);
    }

    private void copyMapperFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(MAPPER_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }

    private void copyDTOFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(DTO_PACKAGE, clazzSimpleName);

        this.copyFile(sourceFolderPath);
    }

    private void copyTestFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(TEST_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }

    private void copyServiceFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(SERVICE_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }

    private void copyRepositoryFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(REPOSITORY_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }

    private void copyResourceFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(RESOURCE_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }

    private void copyDomainFile(String clazzSimpleName) {
        String sourceFolderPath = getSourceFolderPath(DOMAIN_PACKAGE, clazzSimpleName);
        this.copyFile(sourceFolderPath);
    }




    /**
     *  sourcePath alır , buradan kendi sourcedaki dosyasını alır ve targete kopyalar.
     * @param sourcePath
     */
    private void copyFile(  String sourcePath) {
        File source = new File(sourcePath);
        File dest= new File(calculateDestinationPathBySourcePath(sourcePath));
        if(source.exists()) {
            try {
                FileUtils.copyFile(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getClazzSuffixNameByPackageName(String packageName) {
        String clazzSuffix="";
        switch (packageName){
            case RESOURCE_PACKAGE: {
                clazzSuffix = "Resource";
                break;
            }
            case REPOSITORY_PACKAGE:{
                clazzSuffix="Repository";
                break;
            }
            case SERVICE_PACKAGE:{
                clazzSuffix="Service";
                break;
            }
            case TEST_PACKAGE:{
                clazzSuffix="ResourceIntTest";
                break;
            }
            case DTO_PACKAGE:{
                clazzSuffix="DTO";
                break;
            }
            case MAPPER_PACKAGE:{
                clazzSuffix="Mapper";
                break;
            }
        }
        return clazzSuffix;
    }

    private String getCompleteClazzName(String simpleName, String packageName) {
        return  simpleName+getClazzSuffixNameByPackageName(packageName)+".java";
    }

    private String getSourceFolderPath(String packageName, String clazzFileName) {
        if(packageName.equalsIgnoreCase("test")){
            return  SOURCE_PROJECT_FOLDER_BASE_LOCATION +TEST_PROJECT+RESOURCE_PACKAGE+SLASH+getCompleteClazzName(clazzFileName,packageName);
        }
        return SOURCE_PROJECT_FOLDER_BASE_LOCATION +MAIN_PROJECT+packageName+SLASH+getCompleteClazzName(clazzFileName,packageName);
    }

    private String calculateDestinationPathBySourcePath( String sourcePath) {
        String fullClazzName= sourcePath.substring(sourcePath.lastIndexOf(SLASH) + 1);
        String simpleClazzName="";
        if(sourcePath.contains(RESOURCE_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(RESOURCE_PACKAGE))[0];
        }else if(sourcePath.contains(REPOSITORY_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(REPOSITORY_PACKAGE))[0];
        }else if(sourcePath.contains(DTO_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(DTO_PACKAGE))[0];
        }else if(sourcePath.contains(MAPPER_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(MAPPER_PACKAGE))[0];
        }else if(sourcePath.contains(SERVICE_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(SERVICE_PACKAGE))[0];
        }else if(sourcePath.contains(TEST_PACKAGE)){
            simpleClazzName=fullClazzName.split(getClazzSuffixNameByPackageName(TEST_PACKAGE))[0];
        }else{
            simpleClazzName=fullClazzName.split("\\.")[0];
        }
        return ROOT_FILE_PATH+SLASH+simpleClazzName+SLASH+fullClazzName;
    }

    private boolean createNewFolder(String pathForFolder) {
        try {
            FileUtils.deleteDirectory(new File(pathForFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(pathForFolder).mkdirs();

    }

}
