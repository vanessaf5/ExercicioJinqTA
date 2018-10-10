package br.edu.ifpe.jpa.example;

import br.edu.ifpe.jpa.example.entities.Blog;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jinq.jpa.JinqJPAStreamProvider;

import br.edu.ifpe.jpa.example.entities.Car;
import br.edu.ifpe.jpa.example.entities.Post;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.eclipse.persistence.internal.jpa.parsing.jpql.antlr.JPQLParser;
import org.eclipse.persistence.internal.libraries.antlr.runtime.debug.BlankDebugEventListener;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.orm.stream.JinqStream;

public class App {

    static EntityManagerHelper helper = EntityManagerHelper.getInstance();

    public static void main(String[] args) {
        helper.execute(Car.class, streams -> {
            streams
                    .where(c -> c.getPrice() >= 3000)
                    .sortedBy(c -> c.getName())
                    .toList()
                    .stream()
                    .forEach(System.out::println);
        });

    }

    // 1. Imprima na tela todos os blogs que possuem o id maior que 10
    public void questaoUm() {
        helper.execute(Blog.class, streams
                -> streams
                        .where(b -> b.getIdentifier() > 10)
                        .toList()
                        .stream()
                        .forEach(System.out::println)
        );
    }

    // 2. Imprima na tela a descrição do blog que possui o nome "dia a dia, bit a bit"
    public void questaoDois() {
        helper.execute(Blog.class, streams
                -> streams
                        .where(b -> b.getDescription().equals("dia a dia, bit a bit"))
                        .select(b -> b.getDescription())
                        .toList()
                        .stream()
                        .forEach(System.out::println)
        );

    }

    // 3. Imprima na tela as decrições dos 5 primeiros blogs criados (considerar o atributo creationDate)
    public void questaoTres() {
        helper.execute(Blog.class, streams
                -> streams
                        .sortedBy(b -> b.getCreationDate())
                        .limit(5)
                        .select(b ->b.getDescription())
                        .toList()
                        .stream()
                        .forEach(System.out::println)
                
        );

    }

    // 4. Imprima na tela o título e conteúdo de todos os posts do blog com título recebido como parâmetro, 
    //ordenados alfabeticamente pelo título do post
    public void questaoQuatro(String titulo) {
        helper.execute(Post.class, streams ->
        streams 
                .where(p -> p.getBlog().getName().equals(titulo))
                .select( c -> c.getContent() + c.getTitle())
                .toList()
                .stream()
                .forEach(System.out::println)
                
                
        );

    }

    // 5. Imprima na tela o título do último post do blog com título "título"
    public void questaoCinco(String titulo) {
        helper.execute(Post.class,streams ->
                streams
                .where(p -> p.getBlog().getName().equals(titulo))
                .sortedDescendingBy(p -> p.getCreationDate())
                .limit(1)
                .select(p -> p.getTitle())
                .forEach(System.out::println)
                
                
                );

    }

    // 6. Retorne uma lista com os títulos de todos os posts publicados no blog com título tituloBlog 
    //entre o período dataInicial e dataFinal.
    public List<String> questaoSeis(Date dataInicial, Date dataFinal, String tituloBlog) {
            final List<String> list = new ArrayList<>();
         
         helper.execute(Post.class, streams
                ->  { list.addAll(streams
                        .where(p -> p.getBlog().getName().equals(tituloBlog) &&
                                p.getCreationDate().after(dataFinal) && p.getCreationDate().before(dataFinal))
                        .select(p -> p.getTitle()).toList());
        });
        return list;


    }

        


    
    // 7. Imprima na tela a média de posts existentes nos blogs
    public void questaoSete() {
           helper.execute(Blog.class, streams -> {
			double sum = 
			streams
                        .select(b -> b.getPosts().size())
			.reduce((b1,b2) -> b1+b2)
			.get();
			System.out.println(sum/streams.toList().size());
		});
                   }
}  