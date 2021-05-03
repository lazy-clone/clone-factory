package io.vepo.clone.benchmark;

import static java.util.stream.Collectors.joining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import io.vepo.clone.CloneFactory;

public class Benchmark {
    private static final Random RANDOM = new Random();
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    public static void main(String[] args) {
        List<Long> programaticallyTimes = new ArrayList<Long>();
        List<Long> deepTimes = new ArrayList<Long>();
        List<Long> lazyTimes = new ArrayList<Long>();
        List<Long> serializationTimes = new ArrayList<Long>();
        CloneFactory deepCloneFactory = CloneFactory.deep();
        CloneFactory lazyCloneFactory = CloneFactory.lazy();
        IntStream.range(0, 200)
                 .forEach(iteration -> {
//                     List<ComplexPojo> value = IntStream.range(0, 10)
//                                                        .mapToObj(Benchmark::randomObject)
//                                                        .collect(Collectors.toList());
                     ComplexPojo value = randomObject(iteration);
                     deepCloneFactory.clone(value);
                     lazyCloneFactory.clone(value);
                     Runtime.getRuntime().gc();
                     programaticallyTimes.add(measureTime("Programatically",
                                                          () -> new ComplexPojo(value.getId(),
                                                                                new SimplePojo(value.getPojo().getId(),
                                                                                               value.getPojo()
                                                                                                    .getUsername()),
                                                                                value.getIntValues().clone())));
//                     programaticallyTimes.add(measureTime("Programatically",
//                                                          () -> IntStream.range(0, value.size())
//                                                                         .mapToObj(item -> value.get(item))
//                                                                         .map(item -> new ComplexPojo(item.getId(),
//                                                                                                      new SimplePojo(item.getPojo()
//                                                                                                                         .getId(),
//                                                                                                                     item.getPojo()
//                                                                                                                         .getUsername()),
//                                                                                                      item.getIntValues()
//                                                                                                          .clone()))
//                                                                         .collect(Collectors.toList())));
                     Runtime.getRuntime().gc();
                     deepTimes.add(measureTime("Deep", () -> deepCloneFactory.clone(value)));
                     Runtime.getRuntime().gc();
                     lazyTimes.add(measureTime("Lazy", () -> lazyCloneFactory.clone(value)));
                     Runtime.getRuntime().gc();
                     serializationTimes.add(measureTime("Serializing", () -> deepCopyWithObjectSerializer(value)));
                 });

        System.out.println("Programatically average duration: "
                + NUMBER_FORMAT.format(Math.round(programaticallyTimes.stream()
                                                                      .mapToLong(value -> value)
                                                                      .average()
                                                                      .getAsDouble()))
                + "ns");
        System.out.println("Deep average duration: " + NUMBER_FORMAT.format(Math.round(deepTimes.stream()
                                                                                                .mapToLong(value -> value)
                                                                                                .average()
                                                                                                .getAsDouble()))
                + "ns");
        System.out.println("Lazy average duration: " + NUMBER_FORMAT.format(Math.round(lazyTimes.stream()
                                                                                                .mapToLong(value -> value)
                                                                                                .average()
                                                                                                .getAsDouble()))
                + "ns");
        System.out.println("Serializing average duration: "
                + NUMBER_FORMAT.format(Math.round(serializationTimes.stream()
                                                                    .mapToLong(value -> value)
                                                                    .average()
                                                                    .getAsDouble()))
                + "ns");

        write(programaticallyTimes, Paths.get("times-programatically.txt"));
        write(deepTimes, Paths.get("times-deep.txt"));
        write(lazyTimes, Paths.get("times-lazy.txt"));
        write(serializationTimes, Paths.get("times-serialization.txt"));
    }

    private static void write(List<Long> times, Path path) {
        try {
            FileWriter myWriter = new FileWriter(path.toFile());
            myWriter.write(IntStream.range(0, times.size())
                                    .mapToObj(index -> index + "," + times.get(index))
                                    .collect(joining("\n")));
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long measureTime(String operation, Runnable exec) {
        long start = System.nanoTime();
        exec.run();
        long duration = System.nanoTime() - start;
        System.out.println("[" + operation + "] Duration: " + NUMBER_FORMAT.format(duration) + "ns");
        return duration;
    }

    private static ComplexPojo randomObject(int index) {
        return new ComplexPojo(index, new SimplePojo(index + RANDOM.nextInt(), "pojo-" + index), new int[] {
            RANDOM.nextInt(),
            RANDOM.nextInt(),
            RANDOM.nextInt(),
            RANDOM.nextInt(),
            RANDOM.nextInt(),
            RANDOM.nextInt(),
            RANDOM.nextInt() });
    }

    private static Object deepCopyWithObjectSerializer(Object source) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(source);
            oos.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
