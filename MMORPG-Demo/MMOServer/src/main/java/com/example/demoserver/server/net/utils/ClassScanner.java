package com.example.demoserver.server.net.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

    public class ClassScanner {



        private static final String RESOURCE_PATTERN = "/**/*.class";

        private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        private List<String> packages = new LinkedList<>();

        private List<TypeFilter> typeFilters = new LinkedList<>();

        private Set<Class<?>> classSet = new HashSet<>();

        /**
         * 构造函数。
         *
         * @param packages 指定哪些包需要被扫描，支持多个包"package.a,package.b"并对每个包都会递归搜索
         * @param filters  类型过滤器，支持多个
         *
         * @since 1.0.0
         */
        public ClassScanner(String[] packages, TypeFilter... filters) {
            if (packages != null) {
                Collections.addAll(this.packages, packages);
            }

            if (filters != null) {
                this.typeFilters.addAll(Arrays.asList(filters));
            }
        }

        /**
         * 构造函数。
         *
         * @param packages 指定哪些包需要被扫描，支持多个包"package.a,package.b"并对每个包都会递归搜索
         * @param classes  指定扫描包中含有特定类型的bean，支持 Annotation 和 普通类型
         *
         * @since 1.0.0
         */
        @SuppressWarnings("unchecked")
        public ClassScanner(String[] packages, Class<?>... classes) {
            if (packages != null) {
                Collections.addAll(this.packages, packages);
            }

            if (classes != null) {
                for (Class<?> clazz : classes) {

                    //注解类型过滤器，AnnotationTypeFilter
                    TypeFilter filter = clazz.isAnnotation() ? new AnnotationTypeFilter((Class<? extends Annotation>) clazz, false) : new AssignableTypeFilter(clazz);

                    this.typeFilters.add(filter);
                }
            }
        }

        /**
         * 返回符合条件的Class集合。
         *
         * @return Class集合
         *
         * @throws IOException
         * @throws ClassNotFoundException
         * @since 1.0.0
         */
        public Set<Class<?>> getClassSet() throws IOException, ClassNotFoundException {
            this.classSet.clear();

            if (!this.packages.isEmpty()) {
                for (String pkg : this.packages) {
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                    Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            MetadataReader reader = readerFactory.getMetadataReader(resource);
                            String className = reader.getClassMetadata().getClassName();
                            if (matchesEntityTypeFilter(reader, readerFactory)) {

                                this.classSet.add(Class.forName(className));
                            }
                        }
                    }
                }
            }

            // 输出日志
            /** if (log.isInfoEnabled()) {
             for (Class<?> clazz : this.classSet) {
             log.info("Found class: {}", clazz.getName());
             }
             }*/

            return this.classSet;
        }

        /** 检查当前扫描到的class含有任何一个指定的类型过滤器 */
        private boolean matchesEntityTypeFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
            if (!this.typeFilters.isEmpty()) {
                for (TypeFilter filter : this.typeFilters) {
                    if (filter.match(reader, readerFactory)) {
                        return true;
                    }
                }
            }

            return false;
        }

}
