/**
 * Created by gdepuille on 28/09/14.
 */

'use strict';

angular.module('nilsArmoireApp', ['ngAnimate', 'ngTouch', 'uz.mailto'])
    .controller('MainCtrl', function ($scope, $http, $window, Mailto) {

        // Categorie pour le menu
        $scope.categories = {};

        // Categorie selectionne
        $scope.catName = "";
        $scope.currentCategorie = {};

        // initial image index
        $scope.idx = 0;

        // Select categorie
        $scope.selectCategorie = function (key) {
            $scope.catName = key;
            $scope.currentCategorie = $scope.categories[key];
            $scope.showPhoto(0);
        };

        // if a current image is the same as requested image
        $scope.isActive = function (index) {
            return $scope.idx === index;
        };

        // show prev image
        $scope.showPrev = function () {
            $scope.idx = ($scope.idx > 0) ? --$scope.idx : $scope.currentCategorie.photos.length - 1;
        };

        // show next image
        $scope.showNext = function () {
            $scope.idx = ($scope.idx < $scope.currentCategorie.photos.length - 1) ? ++$scope.idx : 0;
        };

        // show a certain image
        $scope.showPhoto = function (index) {
            $scope.idx = index;
        };

        // Send eMail from OS client
        $scope.reserve = function () {
            var articleRef = $scope.currentCategorie.photos[$scope.idx].ref;
            var articleTitle = $scope.currentCategorie.photos[$scope.idx].desc.title;

            var recepient = "amandine.daigle@gmail.com";
            var options = {
                cc: "gregory.depuille@gmail.com",
                subject: "Réservation article : " + articleTitle + " [" + articleRef + "] : ",
                body: "Bonjour,\n\nJe souhaite réserver l'article " + articleTitle + " [" + articleRef +"].\n\nCordialement."
            };

            $window.location = Mailto.url(recepient, options);
        }

        // Chargement des catégorie de photos
        $http({method: 'GET', url: '/categorie'}).
            success(function(data, status, headers, config) {
                $scope.categories = data;
                for (var key in data) {
                    $scope.selectCategorie(key);
                    break;
                }
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
    });

