package com.example.taxi.services;

import com.example.taxi.models.Image;
import com.example.taxi.models.Trip;
import com.example.taxi.models.User;
import com.example.taxi.repositories.TripRepository;
import com.example.taxi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripService {

    @Autowired
    private TripRepository tripRepository;


    public List<Trip> findByCitiesAndAutoClass(String depCity, String arrCity, String autoClass) {

        // Если не указаны города и класс полета, возвращаем все записи
        if (depCity == null && arrCity == null && autoClass == null) {
            return tripRepository.findAll();
        }

        // Если указан только класс полета
        if (depCity == null && arrCity == null) {
            return tripRepository.findByAutoClass(autoClass);
        }

        // Если указан только город прибытия и класс полета
        if (depCity == null && autoClass != null) {
            return tripRepository.findByArrCityAndAutoClass(arrCity, autoClass);
        }

        // Если указан только город отправления и класс полета
        if (arrCity == null && autoClass != null) {
            return tripRepository.findByDepCityAndAutoClass(depCity, autoClass);
        }

        // Если указаны оба города и класс полета
        if (autoClass != null) {
            return tripRepository.findByDepCityAndArrCityAndAutoClass(depCity, arrCity, autoClass);
        }

        // Если указаны только города (без класса полета)
        if (autoClass == null) {
            if (depCity == null) {
                return tripRepository.findByArrCity(arrCity);
            }
            if (arrCity == null) {
                return tripRepository.findByDepCity(depCity);
            }
            return tripRepository.findByDepCityAndArrCity(depCity, arrCity);
        }

        return tripRepository.findAll(); // На случай, если все параметры null (резервный сценарий)
    }



    public void saveTrip(Principal principal, Trip trip, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        trip.setUser(getUserByPrincipal(principal));

        // Обработка первого изображения (основного)
        if (file1 != null && file1.getSize() != 0) {
            Image image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            trip.addImageToTrip(image1);
        }

        // Обработка второго изображения
        if (file2 != null && file2.getSize() != 0) {
            Image image2 = toImageEntity(file2);
            image2.setPreviewImage(false);
            trip.addImageToTrip(image2);
        }

        // Обработка третьего изображения
        if (file3 != null && file3.getSize() != 0) {
            Image image3 = toImageEntity(file3);
            image3.setPreviewImage(false);
            trip.addImageToTrip(image3);
        }

        log.info("Saving new Product. Title: {}; Author email: {}", trip.getTitle(), trip.getUser().getEmail());

        // Сохранение продукта и установка previewImageId
        Trip tripFromDb = tripRepository.save(trip);
        if (!tripFromDb.getImages().isEmpty()) {
            tripFromDb.setPreviewImageId(tripFromDb.getImages().get(0).getId());
        }
        tripRepository.save(tripFromDb);
    }


    @Autowired
    private UserRepository userRepository; // Добавьте инъекцию репозитория

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName()); // Используйте инстанс репозитория
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }


    public void purchaseTrip(Long id, String userEmail) {
        // Находим билет по ID
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Находим текущего пользователя по email
        User user = userRepository.findByEmail(userEmail);

        // Проверяем, что билет принадлежит администратору
        if (trip.getUser() == null || !trip.getUser().isAdmin()) {
            throw new RuntimeException("Trip is not available for purchase");
        }

        // Переназначаем билет новому владельцу
        trip.setUser(user);
        tripRepository.save(trip); // Сохраняем изменения
    }

    @Transactional
    public void deleteTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        User user = userRepository.findByEmail("ilya@mail.ru");
        trip.setUser(user);
        tripRepository.save(trip);
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }
}

