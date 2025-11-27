package lamdepzai;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    String huongdan = """
            
            <%@ page language="java" contentType="text/html; charset=UTF-8"
                pageEncoding="UTF-8"%>
                  <%@ taglib uri="jakarta.tags.core" prefix="c"%>
            <%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
            <%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="UTF-8">
            <title>Insert title here</title>
            <link
            	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
            	rel="stylesheet">
            </head>
            <body>
            
            <form action="/ff/voucher-management/update" method="post">
            <label></label>\s
            <input type="hidden" value="${l.id}" name="id">
            <label>ma</label>\s
            <input type="text" value="${l.ma }" name="ma">
            <label>ten</label>\s
            <input type="text" value="${l.ten }" name="ten">
            <label>so_luong</label>\s
            <input type="text" value="${l.so_luong }" name="so_luong">
            <label>loai_giam</label>\s
            <input value="${ l.loai_giam}" name="loai_giam">
            
            <button>update</button>
            
            </form>
            ${mss }
            
            <form action="/ff/voucher-management/groups" method="get">
            <label>tim so luong</label>
            <input name="soluongtim">
            <label>timten</label>
            <input name="min">
            <button>tim</button>
            </form >
            
            
            <table>
            
            
            <tr>
            <td>#</td>
            <td>ma</td>
            <td>ten</td>
            <td>so_luong</td>
            <td>loai_giam</td>
            <td></td>
            </tr>
            <c:forEach items="${ls}" var="v" varStatus="s">
            <tr>
            <td>
            ${s.count }
            </td>
            <td>${v.ma }</td>
            <td>${v.ten }</td>
            <td>${v.so_luong }</td>
            <td>${v.loai_giam }</td>
            <td><a href="/ff/voucher-management/groups?tim=${v.id }">đ</a></td>
            </tr>
            </c:forEach>
            
            </table>
            
            </body>
            </html>
            """;

    String huong= """
            
            //import jakarta.persistence.EntityManager;
            //import jakarta.persistence.EntityManagerFactory;
            //import jakarta.persistence.Persistence;
            //import lamdepzai.query.RepositoryFactory;
            //
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ASMJAVA4v2");
            //GenericRepository<Room, Integer> roomRepository = new GenericRepositoryImpl<>(emf, Room.class);
            //List<Room> rooms = roomRepository.findAll();
            //roomRepository.save(room);
            //roomRepository.delete(room);
            //
            //
            //
            //EntityManagerFactory emf = Persistence.createEntityManagerFactory("ASMJAVA4v2");
            //EntityManager em = emf.createEntityManager();
            //
            //UserRepository userRepository = RepositoryFactory.createRepository(UserRepository.class, em, User.class);
            //
            //Optional<User> user = userRepository.findByUsernameOrEmail("someone@example.com");
            //
            //
            //
            """;

}