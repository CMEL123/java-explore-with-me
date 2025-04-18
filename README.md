# java-explore-with-me
Template repository for ExploreWithMe project.


Реализация Доп. функции Комментарий к событию
1) для Public API
- Возможность получения комментариев для определенного события (/events/{eventId}/comments)
- Возможность получить определенный комментарий определенного события (/events/{eventId}/comments/{commentId})
2) Для Private
- Получить все свои комментарии (/users/#{userId}/comments)
- Возможность добавить комментарий к событию (/users/#{userId}/events/{eventId}/comments/)
- Возможность редактировать сущ. комментарий (/users/#{userId}/comments/#{commentId})
- Удалить комментарий (/users/#{userId}/comments/#{commentId})
3) Для Admin
- Получить любой комментарий (admin/comments/#{commentId})
- Удалить любой комментарий (admin/comments/#{commentId})
- Изменить любой комментарий (admin/comments/#{commentId})

Ссылка на Pull requests
https://github.com/CMEL123/java-explore-with-me/pull/4