package com.evolutivelabs.app.counter.common.utils;

import com.evolutivelabs.app.counter.common.model.Directory;
import com.evolutivelabs.app.counter.common.model.excelpaser.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 目錄搜尋工具
 */
public class PathDirUtils {
    private static final Logger logger = LoggerFactory.getLogger(PathDirUtils.class);

    public static <T> Condition<T> condition() {
        return new Condition<>();
    }

    /**
     * 搜尋條件
     * @param <T>
     */
    public static class Condition<T> {
        private String[] prefix;
        private String[] suffix;
        private Consumer<T> directoryConsumer;

        private <T> Condition() {}

        public Condition<T> prefix(String... prefix) {
            this.prefix = prefix;
            return this;
        }

        public Condition<T> suffix(String... suffix) {
            this.suffix = suffix;
            return this;
        }

        public Condition<T> consumer(Consumer<T> directoryConsumer) {
            this.directoryConsumer = directoryConsumer;
            return this;
        }
    }

    public static <D extends Directory> D dirProcess(Path path, Class<D> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return dirProcess(path, clazz, null);
    }

    public static <D extends Directory> D dirProcess(Path path, Class<D> clazz, Condition<D> condition) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        boolean isExists = Files.exists(path);
        D directory = clazz.getDeclaredConstructor().newInstance();
        if (isExists) {
            directory.setName(path.getFileName().toString());
            directory.setPath(path.toString());
            directory.setIsDir(Files.isDirectory(path));
            getAttribute(path).ifPresent(attribute -> {
                directory.setSize(attribute.size());
                directory.setLastModified(LocalDateTime.ofInstant(attribute.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
            });
            if (directory.getIsDir()) {
                directory.setChildren(children(path, clazz, condition));
            } else {
                getExtension(directory.getName())
                        .ifPresent(type -> directory.setType(type));
            }

            // 判斷是否額外處理檔案
            if (isProcess(directory, condition)) condition.directoryConsumer.accept(directory);
        }
        return directory;
    }

    private static boolean isProcess(Directory directory, Condition condition) {
        // 沒有任何條件時，不進入額外處理流程
        if (condition == null) return false;

        if (condition.directoryConsumer == null) return false;
        if (directory.getIsDir()) return true;
        if (condition.prefix != null && condition.prefix.length > 0
                && condition.suffix != null && condition.suffix.length > 0) {
            boolean preCheck = false;
            boolean sufCheck = false;
            for (String suffix_col : condition.suffix) {
                if (directory.getName().endsWith(suffix_col)) {
                    sufCheck = true;
                    break;
                }
            }
            for (String prefix_col : condition().prefix) {
                if (directory.getName().startsWith(prefix_col)) {
                    preCheck = true;
                    break;
                }
            }
            return sufCheck && preCheck;
        }

        if (condition.suffix != null && condition.suffix.length > 0) {
            for (String suffix_col : condition.suffix) {
                if (directory.getName().endsWith(suffix_col)) return true;
            }
            return false;
        }
        if (condition.prefix != null && condition.prefix.length > 0) {
            for (String prefix_col : condition().prefix) {
                if (directory.getName().startsWith(prefix_col)) return true;
            }
            return false;
        }

        return true;
    }



    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private static <D extends Directory> List<D> children(Path root, Class<D> clazz, Condition<D> condition) {
        try (Stream<Path> list = Files.list(root)) {
            if (Files.isDirectory(root)) {
                return list.map(children -> {
                            try {
                                return dirProcess(children, clazz, condition);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .filter(dir -> dir != null)
                        .filter(dir -> {
                            // 是資料夾直接pass
                            if (dir.getIsDir()) return true;
                            // 無條件直接pass
                            if (condition == null) return true;
                            // 前綴、後綴不為空，檢核是否符合
                            if (condition.suffix != null && condition.suffix.length > 0
                                    && condition.prefix != null && condition.prefix.length > 0) {
                                boolean preCheck = false;
                                boolean sufCheck = false;
                                for (String suffix_col : condition.suffix) {
                                    if (dir.getName().endsWith(suffix_col)) {
                                        sufCheck = true;
                                        break;
                                    }
                                }
                                for (String prefix_col : condition().prefix) {
                                    if (dir.getName().startsWith(prefix_col)) {
                                        preCheck = true;
                                        break;
                                    }
                                }
                                return sufCheck && preCheck;
                            }

                            // 後綴不為空，檢核是否符合
                            if (condition.suffix != null && condition.suffix.length > 0) {
                                for (String suffix_col : condition.suffix) {
                                    if (dir.getName().endsWith(suffix_col)) return true;
                                }
                                return false;
                            }
                            // 前綴不為空，檢核是否符合
                            if (condition.prefix != null) {
                                for (String prefix_col : condition.prefix) {
                                    if (dir.getName().startsWith(prefix_col)) return true;
                                }
                                return false;
                            }

                            return true;
                        })
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    private static Optional<BasicFileAttributes> getAttribute(Path path) {
        try {
            return Optional
                    .of(Files.getFileAttributeView(path, BasicFileAttributeView.class)
                            .readAttributes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
