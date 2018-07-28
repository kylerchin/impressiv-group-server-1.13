package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkMap extends Long2ObjectOpenHashMap<Chunk> {

    private static final Logger a = LogManager.getLogger();

    public ChunkMap(int i) {
        super(i);
    }

    public Chunk a(long i, Chunk chunk) {
        Chunk chunk1 = (Chunk) super.put(i, chunk);
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i);

        for (int j = chunkcoordintpair.x - 1; j <= chunkcoordintpair.x + 1; ++j) {
            for (int k = chunkcoordintpair.z - 1; k <= chunkcoordintpair.z + 1; ++k) {
                if (j != chunkcoordintpair.x || k != chunkcoordintpair.z) {
                    long l = ChunkCoordIntPair.a(j, k);
                    Chunk chunk2 = (Chunk) this.get(l);

                    if (chunk2 != null) {
                        chunk.H();
                        chunk2.H();
                    }
                }
            }
        }

        org.bukkit.Server server = chunk.world.getServer();
        if (server != null) {
            /*
             * If it's a new world, the first few chunks are generated inside
             * the World constructor. We can't reliably alter that, so we have
             * no way of creating a CraftWorld/CraftServer at that point.
             */
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(chunk.bukkitChunk, chunk.newChunk));
        }

        // Update neighbor counts
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                Chunk neighbor = this.get(ChunkCoordIntPair.a(chunkcoordintpair.x + x, chunkcoordintpair.z + z));
                if (neighbor != null) {
                    neighbor.setNeighborLoaded(-x, -z);
                    chunk.setNeighborLoaded(x, z);
                }
            }
        }

        if (chunk.newChunk) {
            BlockSand.instaFall = true;
            java.util.Random random = new java.util.Random();
            random.setSeed(chunk.world.getSeed());
            long xRand = random.nextLong() / 2L * 2L + 1L;
            long zRand = random.nextLong() / 2L * 2L + 1L;
            random.setSeed((long) chunk.locX * xRand + (long) chunk.locZ * zRand ^ chunk.world.getSeed());

            org.bukkit.World world = chunk.world.getWorld();
            if (world != null) {
                chunk.world.populating = true;
                try {
                    for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                        populator.populate(world, random, chunk.bukkitChunk);
                    }
                } finally {
                    chunk.world.populating = false;
                }
            }
            BlockSand.instaFall = false;
            chunk.world.getServer().getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(chunk.bukkitChunk));
        }

        return chunk1;
    }

    public Chunk a(Long olong, Chunk chunk) {
        return this.a(olong.longValue(), chunk);
    }

    public Chunk a(long i) {
        Chunk chunk = (Chunk) super.remove(i);
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i);

        for (int j = chunkcoordintpair.x - 1; j <= chunkcoordintpair.x + 1; ++j) {
            for (int k = chunkcoordintpair.z - 1; k <= chunkcoordintpair.z + 1; ++k) {
                if (j != chunkcoordintpair.x || k != chunkcoordintpair.z) {
                    Chunk chunk1 = (Chunk) this.get(ChunkCoordIntPair.a(j, k));

                    if (chunk1 != null) {
                        chunk1.I();
                    }
                }
            }
        }

        return chunk;
    }

    public Chunk a(Object object) {
        return this.a(((Long) object).longValue());
    }

    public void putAll(Map<? extends Long, ? extends Chunk> map) {
        throw new RuntimeException("Not yet implemented");
    }

    public boolean remove(Object object, Object object1) {
        throw new RuntimeException("Not yet implemented");
    }

    // CraftBukkit start - decompile errors
    public Chunk remove(Object object) {
        return this.a(object);
    }

    public Chunk remove(long i) {
        return this.a(i);
    }

    public Chunk put(Long olong, Chunk object) {
        return this.a(olong, (Chunk) object);
    }

    public Chunk put(long i, Chunk object) {
        return this.a(i, (Chunk) object);
    }

    public Object put(Object object, Chunk object1) {
        return this.a((Long) object, (Chunk) object1);
    }
    // CraftBukkit end
}
