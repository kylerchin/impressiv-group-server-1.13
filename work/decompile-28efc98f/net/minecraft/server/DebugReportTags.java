package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DebugReportTags<T> implements DebugReportProvider {

    private static final Logger e = LogManager.getLogger();
    private static final Gson f = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DebugReportGenerator b;
    protected final RegistryMaterials<MinecraftKey, T> c;
    protected final Map<Tag<T>, Tag.a<T>> d = Maps.newLinkedHashMap();

    protected DebugReportTags(DebugReportGenerator debugreportgenerator, RegistryMaterials<MinecraftKey, T> registrymaterials) {
        this.b = debugreportgenerator;
        this.c = registrymaterials;
    }

    protected abstract void b();

    public void a(HashCache hashcache) throws IOException {
        this.d.clear();
        this.b();
        Tags tags = new Tags((minecraftkey) -> {
            return false;
        }, (minecraftkey) -> {
            return null;
        }, "", false, "generated");
        Iterator iterator = this.d.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            MinecraftKey minecraftkey = ((Tag) entry.getKey()).c();
            Tag.a tag_a = (Tag.a) entry.getValue();

            tags.getClass();
            if (!tag_a.a(tags::a)) {
                throw new UnsupportedOperationException("Unsupported referencing of tags!");
            }

            Tag tag = ((Tag.a) entry.getValue()).b(minecraftkey);
            RegistryMaterials registrymaterials = this.c;

            this.c.getClass();
            JsonObject jsonobject = tag.a(registrymaterials::b);
            java.nio.file.Path java_nio_file_path = this.a(minecraftkey);

            tags.a(tag);
            this.a(tags);

            try {
                String s = DebugReportTags.f.toJson(jsonobject);
                String s1 = DebugReportTags.a.hashUnencodedChars(s).toString();

                if (!Objects.equals(hashcache.a(java_nio_file_path), s1) || !Files.exists(java_nio_file_path, new LinkOption[0])) {
                    Files.createDirectories(java_nio_file_path.getParent(), new FileAttribute[0]);
                    BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path, new OpenOption[0]);
                    Throwable throwable = null;

                    try {
                        bufferedwriter.write(s);
                    } catch (Throwable throwable1) {
                        throwable = throwable1;
                        throw throwable1;
                    } finally {
                        if (bufferedwriter != null) {
                            if (throwable != null) {
                                try {
                                    bufferedwriter.close();
                                } catch (Throwable throwable2) {
                                    throwable.addSuppressed(throwable2);
                                }
                            } else {
                                bufferedwriter.close();
                            }
                        }

                    }
                }

                hashcache.a(java_nio_file_path, s1);
            } catch (IOException ioexception) {
                DebugReportTags.e.error("Couldn\'t save tags to {}", java_nio_file_path, ioexception);
            }
        }

    }

    protected abstract void a(Tags<T> tags);

    protected abstract java.nio.file.Path a(MinecraftKey minecraftkey);

    protected Tag.a<T> a(Tag<T> tag) {
        return (Tag.a) this.d.computeIfAbsent(tag, (tag) -> {
            return Tag.a.a();
        });
    }
}
