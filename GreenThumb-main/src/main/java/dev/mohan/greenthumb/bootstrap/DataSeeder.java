package dev.mohan.greenthumb.bootstrap;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.mohan.greenthumb.domain.Grower;
import dev.mohan.greenthumb.domain.Plant;
import dev.mohan.greenthumb.domain.PlantType;
import dev.mohan.greenthumb.domain.Role;
import dev.mohan.greenthumb.domain.User;
import dev.mohan.greenthumb.repository.GrowerRepository;
import dev.mohan.greenthumb.repository.PlantRepository;
import dev.mohan.greenthumb.repository.PlantTypeRepository;
import dev.mohan.greenthumb.repository.RoleRepository;
import dev.mohan.greenthumb.repository.UserRepository;

/**
 * Seeds sample data on startup so the read endpoints have something to return.
 * Each block is guarded by a count() check, so re-running the app won't create duplicates.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final GrowerRepository growerRepository;
    private final PlantTypeRepository plantTypeRepository;
    private final PlantRepository plantRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // All three repositories are injected by Spring via this constructor.
    public DataSeeder(GrowerRepository growerRepository,
                      PlantTypeRepository plantTypeRepository,
                      PlantRepository plantRepository, 
                      RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.growerRepository = growerRepository;
        this.plantTypeRepository = plantTypeRepository;
        this.plantRepository = plantRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // 1. Growers (only if none exist yet)
        if (growerRepository.count() == 0) {
            growerRepository.save(makeGrower("Sunrise Nurseries", "Premium indoor plants", 10));
            growerRepository.save(makeGrower("Urban Roots", "City-friendly greenery", 20));
            growerRepository.save(makeGrower("Fern & Foliage", "Lush shade plants", 30));
        }

        // 2. Plant types (only if none exist yet)
        if (plantTypeRepository.count() == 0) {
            plantTypeRepository.save(makePlantType("Indoor", "Thrives inside the home", 10));
            plantTypeRepository.save(makePlantType("Outdoor", "Loves direct sunlight", 20));
            plantTypeRepository.save(makePlantType("Succulents", "Low water needs", 30));
        }

        // 3. Plants (only if none exist yet) — attach EXISTING growers + types
        if (plantRepository.count() == 0) {
            List<Grower> growers = growerRepository.findAll();
            List<PlantType> types = plantTypeRepository.findAll();

            Grower sunrise = growers.get(0);
            Grower urbanRoots = growers.get(1);
            PlantType indoor = types.get(0);
            PlantType succulents = types.get(2);

            plantRepository.save(makePlant(
                    "Monstera Deliciosa", "https://example.com/monstera.jpg",
                    40.0, 30.0, 10, sunrise, indoor));

            plantRepository.save(makePlant(
                    "Snake Plant", "https://example.com/snake.jpg",
                    25.0, 20.0, 20, sunrise, indoor));

            plantRepository.save(makePlant(
                    "Aloe Vera", "https://example.com/aloe.jpg",
                    15.0, 12.0, 30, urbanRoots, succulents));
        }

        if (roleRepository.count() == 0) {
            Role user = new Role();  
            user.setName("ROLE_USER");   
            roleRepository.save(user);
            
            Role admin = new Role(); 
            admin.setName("ROLE_ADMIN");  
            roleRepository.save(admin);
        }

        if (!userRepository.existsByEmail("admin@example.com")) {
            Role adminRole = roleRepository.findById("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not seeded"));
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setActive(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }
    }

    // ---- helper builders ----

    private Grower makeGrower(String name, String description, int sortOrder) {
        Grower g = new Grower();
        g.setName(name);
        g.setDescription(description);
        g.setSortOrder(sortOrder);
        return g;
    }

    private PlantType makePlantType(String name, String description, int sortOrder) {
        PlantType pt = new PlantType();
        pt.setName(name);
        pt.setDescription(description);
        pt.setSortOrder(sortOrder);
        return pt;
    }

    private Plant makePlant(String name, String photo, double originalPrice,
                            double sellingPrice, int sortOrder,
                            Grower grower, PlantType plantType) {
        Plant p = new Plant();
        p.setName(name);
        p.setPhoto(photo);
        p.setOriginalPrice(originalPrice);
        p.setSellingPrice(sellingPrice);
        p.setSortOrder(sortOrder);
        p.setGrower(grower);          // attach the @ManyToOne relationships
        p.setPlantType(plantType);
        return p;
    }
}