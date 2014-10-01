/**
 * Created by gdepuille on 28/09/14.
 */

'use strict';

angular.module('nilsArmoireApp', ['ngAnimate', 'ngTouch'])
    .controller('MainCtrl', function ($scope, $http) {

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

