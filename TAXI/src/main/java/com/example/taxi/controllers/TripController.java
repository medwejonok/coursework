package com.example.taxi.controllers;
import com.example.taxi.repositories.UserRepository;
import com.example.taxi.models.Trip;
import com.example.taxi.models.User;
import com.example.taxi.services.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TripController {
    private final UserRepository userRepository;
    private final TripService tripService;

    @GetMapping("/")
    public String trips(@RequestParam(name = "depCity", required = false) String depCity,
                        @RequestParam(name = "arrCity", required = false) String arrCity,
                        @RequestParam(name = "autoClass", required = false) String autoClass,
                           Principal principal,
                           Model model) {


        // Если город не выбран, то устанавливаем его в null
        if (depCity != null && depCity.isEmpty()) {
            depCity = null;
        }
        if (arrCity != null && arrCity.isEmpty()) {
            arrCity = null;
        }
        if (autoClass != null && autoClass.isEmpty()) {
            autoClass = null;
        }


        // Получаем список продуктов, применяя фильтрацию
        List<Trip> trips = tripService.findByCitiesAndAutoClass(depCity, arrCity, autoClass);



        // Добавляем данные в модель для отображения в шаблоне
        model.addAttribute("trips", trips);
        model.addAttribute("user", tripService.getUserByPrincipal(principal));
        model.addAttribute("depCity", depCity);  // передаем выбранные города
        model.addAttribute("arrCity", arrCity);
        model.addAttribute("autoClass", autoClass);

        return "trips"; // Страница с отображением списка продуктов
    }



    @PostMapping("/trip/create")
    public String createTrip(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3,
            Trip trip,
            Principal principal) throws IOException {
        tripService.saveTrip(principal, trip, file1, file2, file3);
        return "redirect:/my/trips";
    }

    @PostMapping("/trip/purchase/{id}")
    public String purchaseTrip(@PathVariable Long id, Principal principal) {
        String email = principal.getName(); // Получаем email текущего пользователя
        tripService.purchaseTrip(id, email);
        return "redirect:/my/trips";
    }

    @PostMapping("/trip/delete/{id}")
    public String deleteTrip(@PathVariable Long id) {
        tripService.deleteTripById(id);
        return "redirect:/my/trips";
    }

    @GetMapping("/my/trips")
    public String userTrips(Principal principal, Model model) {
        User user = tripService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("trips", user.getTrips());
        return "my-trips";
    }

    @GetMapping("/trip/{id}")
    public String tripInfo(@RequestParam(name = "showButton", defaultValue = "true") boolean showButton,
                           @PathVariable Long id, Model model, Principal principal) {
        // Получаем информацию о продукте и авторе
        Trip trip = tripService.getTripById(id);
        User author = trip.getUser();

        // Добавляем данные в модель
        model.addAttribute("showButton", showButton);
        model.addAttribute("trip", trip);
        model.addAttribute("images", trip.getImages()); // Изображения продукта
        model.addAttribute("authorTrip", author);
        model.addAttribute("user", tripService.getUserByPrincipal(principal)); // Пользователь, просматривающий страницу

        return "trip-info";
    }
}
